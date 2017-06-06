package com.broccoli.backend

import anorm._
import anorm.SqlParser._
import com.broccoli.backend.auth.{AuthenticationSupport, JWTClaims}
import org.blinkmob.scalatraseed.{DB, DBW}
import org.scalatra.{Ok, Forbidden, BadRequest, Conflict, InternalServerError}
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.mysql.jdbc.MysqlDataTruncation
import authentikat.jwt._
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.reflect.runtime.universe._

case class AllQuizzesRequest(bookID:Int)
case class ImagesAndQuizzesRawResponse(pageNumber:Int,
                                          uri:String,
                                          xPercent:Double,
                                          yPercent:Double,
                                          widthPercent:Double,
                                          heightPercent:Double,
                                          quizID:Option[Int],
                                          languageID:Option[Int],
                                          language:Option[String],
                                          questionID:Option[Int],
                                          question:Option[String],
                                          responseID:Option[Int],
                                          response:Option[String],
                                          responseIsCorrect:Option[Long])
case class Response(id:Int, text:String, isCorrect:Boolean)
case class Question(id:Int, text:String, responses:Set[Response])
case class QuizInLanguage(id:Int, languageID:Int, language:String, questions:Set[Question])
case class Image(uri:String, xPercent:Double, yPercent:Double, widthPercent:Double, heightPercent:Double, quizzes:Option[Set[QuizInLanguage]])
case class Page(pageNumber:Int, images:Set[Image])

case class MusicRequest(bookID:Int)
case class MusicRawResponse(pageNumber:Int, uri:String)

case class UserResponse(chosenResponses:List[Int])


/**
 * @author anthonygabriele
 */
class QuizServlet(db:DBW) extends BroccolistudentsStack with AuthenticationSupport{
  
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
        db.run{implicit c =>
          var raw:List[ImagesAndQuizzesRawResponse] = SQL"""
            select bp.PAGE_NUMBER,
                    IMAGES.URI, IMAGES.X_PERCENT, IMAGES.Y_PERCENT, IMAGES.WIDTH_PERCENT, IMAGES.HEIGHT_PERCENT,
                    QUIZZES.QUIZ_ID, 
                    LANGUAGES.LANGUAGE_ID,
                    LANGUAGES.NAME as QUIZ_LANGUAGE,
                    QUIZ_QUESTIONS.QUIZ_QUESTION_ID,
                    QUIZ_QUESTION_TEXT.TEXT as QUESTION_TEXT,
                    QUIZ_RESPONSES.QUIZ_RESPONSE_ID,
                    QUIZ_RESPONSE_TEXT.TEXT as RESPONSE_TEXT,
                    QUIZ_CORRECT_RESPONSES.QUIZ_RESPONSE_ID=QUIZ_RESPONSE_TEXT.QUIZ_RESPONSE_ID as RESPONSE_IS_CORRECT
            from BOOK_PAGE_IMAGES
            inner join (
              select BOOK_PAGE_ID, PAGE_NUMBER from BOOK_PAGES where BOOK_ID=1
            ) bp on BOOK_PAGE_IMAGES.BOOK_PAGE_ID=bp.BOOK_PAGE_ID
            left join IMAGES on IMAGES.IMAGE_ID=BOOK_PAGE_IMAGES.IMAGE_ID
            left join QUIZZES on QUIZZES.IMAGE_ID=IMAGES.IMAGE_ID
            left join LANGUAGE_QUIZZES on LANGUAGE_QUIZZES.QUIZ_ID=QUIZZES.QUIZ_ID
            left join LANGUAGES on LANGUAGE_QUIZZES.LANGUAGE_ID=LANGUAGES.LANGUAGE_ID
            left join QUIZ_QUESTIONS on QUIZ_QUESTIONS.QUIZ_ID=QUIZZES.QUIZ_ID
            left join QUIZ_QUESTION_TEXT on QUIZ_QUESTIONS.QUIZ_QUESTION_ID=QUIZ_QUESTION_TEXT.QUIZ_QUESTION_ID 
              and QUIZ_QUESTION_TEXT.LANGUAGE_QUIZ_ID=LANGUAGE_QUIZZES.LANGUAGE_QUIZ_ID
            left join QUIZ_RESPONSES on QUIZ_RESPONSES.QUIZ_QUESTION_ID = QUIZ_QUESTIONS.QUIZ_QUESTION_ID
            left join QUIZ_RESPONSE_TEXT on QUIZ_RESPONSES.QUIZ_RESPONSE_ID=QUIZ_RESPONSE_TEXT.QUIZ_RESPONSE_ID
              and QUIZ_RESPONSE_TEXT.LANGUAGE_QUIZ_ID=LANGUAGE_QUIZZES.LANGUAGE_QUIZ_ID
            left join QUIZ_CORRECT_RESPONSES on QUIZ_RESPONSES.QUIZ_RESPONSE_ID=QUIZ_CORRECT_RESPONSES.QUIZ_RESPONSE_ID
          """.as(Macro.indexedParser[ImagesAndQuizzesRawResponse].*)
          val squashResponses = squash(
              raw,
              (a:ImagesAndQuizzesRawResponse) => 
                  (a.pageNumber, a.uri, a.xPercent, a.yPercent, 
                    a.widthPercent, a.heightPercent, a.quizID, 
                    a.languageID, a.language, a.questionID, a.question),
              (a:ImagesAndQuizzesRawResponse) => a.quizID match{
                case Some(quizID) => Some(Response(a.responseID.get, a.response.get, a.responseIsCorrect == Some(1)))
                case None => None
              },
              (b:(Int, String, Double, Double, Double, Double, Option[Int], Option[Int], Option[String], Option[Int], Option[String]),
               c:List[Option[Response]]) => (
                  b._1, b._2, b._3, b._4, b._5, b._6, b._7,
                  b._8, b._9, b._10, b._11, sequence(c)
              )
          )
          val squashQuestions = squash(
              squashResponses,
              (a:(Int, String, Double, Double, Double, Double,
                  Option[Int], Option[Int], Option[String], Option[Int], Option[String], Option[Set[Response]])) => 
                (a._1, a._2, a._3, a._4, a._5, a._6, a._7, a._8, a._9),
              (a:(Int, String, Double, Double, Double, Double,
                  Option[Int], Option[Int], Option[String], Option[Int], Option[String], Option[Set[Response]])) => {
                a._7 match{
                  case Some(quizID) => {
                    val questionID = a._10.get
                    val questionText = a._11.get
                    val responses = a._12.get
                    Some(Question(questionID, questionText, responses))
                  }
                  case None => None
                }
              },
              (b:(Int, String, Double, Double, Double, Double,
                  Option[Int], Option[Int], Option[String]),
               c:List[Option[Question]]) => {
                (b._1, b._2, b._3, b._4, b._5, b._6,
                    b._7, b._8, b._9, sequence(c))
              }
          )
          val squashQuizzes = squash(
              squashQuestions,
              (a:(Int, String, Double, Double, Double, Double,
                  Option[Int], Option[Int], Option[String], Option[Set[Question]])) => 
                (a._1, a._2, a._3, a._4, a._5, a._6),
              (a:(Int, String, Double, Double, Double, Double,
                  Option[Int], Option[Int], Option[String], Option[Set[Question]])) => {
                a._7 match {
                  case Some(quizID) => {
                    val languageID = a._8.get
                    val language = a._9.get
                    val questions = a._10.get
                    Some(QuizInLanguage(quizID, languageID, language, questions))
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
  post("/music"){
    val userID = jwtAuth.get.userid
    parsedBody.extractOpt[MusicRequest] match {
      case Some(mr) => {
        db.run{implicit c =>
          var raw:List[MusicRawResponse] = SQL"""
            select bp.PAGE_NUMBER, MUSIC.URI
            from BOOK_PAGE_MUSIC
            inner join (
              select BOOK_PAGE_ID, PAGE_NUMBER from BOOK_PAGES where BOOK_ID=#${mr.bookID}
            ) bp on BOOK_PAGE_MUSIC.BOOK_PAGE_ID=bp.BOOK_PAGE_ID
            left join MUSIC on MUSIC.MUSIC_ID=BOOK_PAGE_MUSIC.MUSIC_ID
          """.as(Macro.indexedParser[MusicRawResponse].*)
          Ok(raw)
        }
      }
      case None => BadRequest(ErrorMessage("Unable to parse responses"))
    }
  }
  
  post("/userResponse"){
    val userID = jwtAuth.get.userid
    parsedBody.extractOpt[UserResponse] match {
      case Some(ur) => {
        db.run{implicit c =>
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