package com.readbroccoli.broccolistudents

import anorm._
import anorm.SqlParser._
import com.readbroccoli.broccolistudents.auth.{AuthenticationSupport, JWTClaims}
import com.readbroccoli.broccolistudents.utils.DB
import org.scalatra.{Ok, Forbidden, BadRequest, Conflict, InternalServerError}
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.mysql.jdbc.MysqlDataTruncation
import authentikat.jwt._
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.reflect.runtime.universe._

case class AllQuizzesRequest(bookID:String)
case class ImagesAndQuizzesRawResponse(pageNumber:Int,
                                          uri:String,
                                          xPercent:Double,
                                          yPercent:Double,
                                          widthPercent:Double,
                                          heightPercent:Double,
                                          language:Option[String],
                                          quizID:Option[Int],
                                          questionID:Option[Int],
                                          question:Option[String],
                                          response:Option[String],
                                          responseID:Option[Int],
                                          responseIsCorrect:Option[Long])
case class Response(text:String, id:Int, isCorrect:Boolean)
case class Question(text:String, id:Int, responses:Set[Response])
case class QuizInLanguage(id:Int, language:String, questions:Set[Question])
case class Image(uri:String, xPercent:Double, yPercent:Double, widthPercent:Double, heightPercent:Double, quizzes:Option[Set[QuizInLanguage]])
case class Page(pageNumber:Int, images:Set[Image])

case class UserResponse(chosenResponses:List[Int])

/**
 * @author anthonygabriele
 */
class QuizServlet extends BroccolistudentsStack with AuthenticationSupport{
  
  def squash[A, B, C, T]( data:List[A], groupBy:A => T, extract: A => C, convert:(T, List[C]) => B): List[B] = {
    data.groupBy( g => groupBy(g) ).map{
      case (k, v) => {
        val extractedList = v.map( m => extract(m))
        convert(k, extractedList)
      }
    }.toList
  }
  def sequence[A](l: List[Option[A]]) = (Option(List.empty[A].toSet) /: l) {
    case(Some(sofar), Some(value)) => Some(sofar + value); 
    case(_, _) => None 
  }
  
  post("/imagesAndQuizzes"){
    parsedBody.extractOpt[AllQuizzesRequest] match {
      case Some(qr) => {
        DB.conn{implicit c =>
          var raw:List[ImagesAndQuizzesRawResponse] = SQL"""
            select qall.PAGE_NUMBER, qall.URI, qall.X_PERCENT, qall.Y_PERCENT, qall.WIDTH_PERCENT, qall.HEIGHT_PERCENT, qall.LANGUAGE, qall.QUIZ_ID, qall.QUIZ_QUESTION_ID, qall.QUESTION_TEXT, qall.RESPONSE_TEXT, qall.RESPONSE_ID, qall.RESPONSE_ID=qcr.QUIZ_RESPONSE_ID as RESPONSE_IS_CORRECT
            from (
              select qlt.PAGE_NUMBER, qlt.URI, qlt.X_PERCENT, qlt.Y_PERCENT, qlt.WIDTH_PERCENT, qlt.HEIGHT_PERCENT, qlt.QUIZ_ID, qlt.LANGUAGE, qlt.QUIZ_QUESTION_ID, qlt.QUESTION_TEXT, qrt.TEXT as RESPONSE_TEXT, qrt.QUIZ_RESPONSE_ID as RESPONSE_ID
              from (
                select qll.PAGE_NUMBER, qll.URI, qll.X_PERCENT, qll.Y_PERCENT, qll.WIDTH_PERCENT, qll.HEIGHT_PERCENT, qll.QUIZ_ID, qll.LANGUAGE, qqt.TEXT as QUESTION_TEXT, qqt.QUIZ_QUESTION_ID
                from (
                  select q.PAGE_NUMBER, q.URI, q.X_PERCENT, q.Y_PERCENT, q.WIDTH_PERCENT, q.HEIGHT_PERCENT, q.QUIZ_ID, l.NAME as LANGUAGE 
                  from (
                        select QUIZZES.QUIZ_ID, PAGE_NUMBER, IMAGES.IMAGE_ID, URI, X_PERCENT, Y_PERCENT, WIDTH_PERCENT, HEIGHT_PERCENT from IMAGES
                        inner join (
                          select BOOK_PAGE_ID, PAGE_NUMBER from BOOK_PAGES where BOOK_ID=#${qr.bookID}
                        ) bp on IMAGES.BOOK_PAGE_ID = bp.BOOK_PAGE_ID
                        left join QUIZZES on QUIZZES.IMAGE_ID=IMAGES.IMAGE_ID
                  ) q
                  left join LANGUAGE_QUIZZES as lq on q.QUIZ_ID = lq.QUIZ_ID
                  left join LANGUAGES as l on l.LANGUAGE_ID = lq.LANGUAGE_ID
                ) qll
                left join QUIZ_QUESTIONS as qq on qll.QUIZ_ID = qq.QUIZ_ID
                left join QUIZ_QUESTION_TEXT as qqt on qq.QUIZ_QUESTION_ID = qqt.QUIZ_QUESTION_ID
              ) qlt
              left join QUIZ_RESPONSES as qr on qr.QUIZ_QUESTION_ID = qlt.QUIZ_QUESTION_ID
              left join QUIZ_RESPONSE_TEXT as qrt on qrt.QUIZ_RESPONSE_ID = qr.QUIZ_RESPONSE_ID
            ) qall left join QUIZ_CORRECT_RESPONSES as qcr on (qall.QUIZ_QUESTION_ID =  qcr.QUIZ_QUESTION_ID)
          """.as(Macro.indexedParser[ImagesAndQuizzesRawResponse].*)
          val squashResponses = squash(
              raw,
              (a:ImagesAndQuizzesRawResponse) => 
                  (a.pageNumber, a.uri,  a.xPercent,  a.yPercent, 
                    a.widthPercent,  a.heightPercent,  a.language, 
                    a.quizID,  a.questionID,  a.question),
              (a:ImagesAndQuizzesRawResponse) => a.quizID match{
                case Some(quizID) => Some(Response(a.response.get, a.responseID.get, a.responseIsCorrect.get == 1))
                case None => None
              },
              (b:(Int, String, Double, Double, Double, Double, Option[String], Option[Int], Option[Int], Option[String]),
               c:List[Option[Response]]) => (
                  b._1, b._2, b._3, b._4, b._5, b._6, b._7,
                  b._8, b._9, b._10, sequence(c)
              )
          )
          val squashQuestions = squash(
              squashResponses,
              (a:(Int, String, Double, Double, Double, Double,
                  Option[String], Option[Int], Option[Int], Option[String], Option[Set[Response]])) => 
                (a._1, a._2, a._3, a._4, a._5, a._6, a._7, a._8),
              (a:(Int, String, Double, Double, Double, Double,
                  Option[String], Option[Int], Option[Int], Option[String], Option[Set[Response]])) => {
                a._9 match{
                  case Some(questionID) => {
                    val questionText = a._10.get
                    val responses = a._11.get
                    Some(Question(questionText, questionID, responses))
                  }
                  case None => None
                }
              },
              (b:(Int, String, Double, Double, Double, Double,
                  Option[String], Option[Int]),
               c:List[Option[Question]]) => {
                (b._1, b._2, b._3, b._4, b._5,
                    b._6, b._7, b._8, sequence(c))
              }
          )
          val squashQuizzes = squash(
              squashQuestions,
              (a:(Int, String, Double, Double, Double, Double,
                  Option[String], Option[Int], Option[Set[Question]])) => 
                (a._1, a._2, a._3, a._4, a._5, a._6),
              (a:(Int, String, Double, Double, Double, Double,
                  Option[String], Option[Int], Option[Set[Question]])) => {
                a._8 match {
                  case Some(quizID) => {
                    val language = a._7.get
                    val questions = a._9.get
                    Some(QuizInLanguage(quizID, language, questions))
                  }
                  case None => None
                }
              },
              (b:(Int, String, Double, Double, Double, Double),
               c:List[Option[QuizInLanguage]]) => {
                (b._1, b._2, b._3, b._4, b._5, b._6, sequence(c))
              }
          )
          val squashImages = squash(
              squashQuizzes,
              (a:(Int, String, Double, Double, Double, Double, Option[Set[QuizInLanguage]])) => a._1,
              (a:(Int, String, Double, Double, Double, Double, Option[Set[QuizInLanguage]])) => {
                val uri = a._2
                val xPercent = a._3
                val yPercent = a._4
                val widthPercent = a._5
                val heightPercent = a._6
                val quizzesInLanguages = a._7
                Image(uri, xPercent, yPercent, widthPercent, heightPercent, quizzesInLanguages)
              },
              (b:Int,
               c:List[Image]) => {
                 Page(b, c.toSet)
               }
          )
          Ok(squashImages)
        }
      }
      case None => BadRequest(ErrorMessage("Unable to parse requested bookID"))
    }
  }
  
  post("/userResponse"){
    val userID = jwtAuth.get.userid
    parsedBody.extractOpt[UserResponse] match {
      case Some(ur) => {
        DB.conn{implicit c =>
          ur.chosenResponses.foreach(
              (responseID) => {
                try{
                  SQL"""
                    insert into QUIZ_STUDENT_RESPONSES (USER_ID, QUIZ_QUESTION_ID, QUIZ_RESPONSE_ID)
                    values (
                      #${userID},
                      (select QUIZ_QUESTION_ID from QUIZ_RESPONSES where QUIZ_RESPONSE_ID=#${responseID}),
                      #${responseID}
                    );
                  """.executeInsert()
                }catch{
                  case e:MySQLIntegrityConstraintViolationException => Forbidden(ErrorMessage("User has already answered some of the questions")) 
                }
              }
          )
          Ok()
        }
      }
      case None => BadRequest(ErrorMessage("Unable to parse responses"))
    }
  }
  
  /*post("/create"){
    
  }*/
}