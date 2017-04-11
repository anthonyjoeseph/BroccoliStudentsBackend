package com.readbroccoli.broccolistudents

/**
 * @author anthonygabriele
 */

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

//requests
case class RawBook(
    id:Int,
    title:String,
    baseURI:String,
    aspectRatio:Double,
    numPages:Int,
    country:String,
    characterVoiceID:Option[Int],
    characterVoice:Option[String],
    tagID:Int,
    tag:String,
    languageID:Int,
    languageName:String)
case class TextRequest(bookID:Int, languageID:Int)
case class CreateBookRequest(
    title:String, 
    baseURI:String, 
    aspectRatio:Double, 
    country:String,
    characterVoices:Option[List[String]],
    tags:List[String], 
    languages:List[String])
case class AddTextRequest(bookID:Int, languageID:Int, text:List[String])

//responses
case class Book(
    id:Int, 
    title:String, 
    baseURI:String, 
    aspectRatio:Double, 
    numPages:Int,
    country:String,
    characterVoices:Option[Set[CharacterVoice]], 
    tags:Set[Tag], 
    languages:Set[Language])
case class Language(id:Int, name:String)
case class CharacterVoice(id:Int, name:String)
case class PageText(pageNumber:Int, text:String)
case class Tag(id:Int, name:String)

class BookServlet extends BroccolistudentsStack with AuthenticationSupport {
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
  //accessing
  get("/allMeta"){
    DB.conn{implicit c =>
      val raw:List[RawBook] = SQL"""
        select bct.BOOK_ID, bct.TITLE, bct.BASE_URI, bct.ASPECT_RATIO, bct.NUM_PAGES, bct.COUNTRY, bct.CHARACTER_VOICE_ID, bct.CHARACTER_VOICE, bct.TAG_ID, bct.TAG, l.LANGUAGE_ID, l.NAME as LANGUAGE_NAME
        from
        (
          select bcv.BOOK_ID, bcv.TITLE, bcv.BASE_URI, bcv.ASPECT_RATIO, bcv.NUM_PAGES, bcv.COUNTRY, bcv.CHARACTER_VOICE_ID, bcv.CHARACTER_VOICE, t.TAG_ID, t.NAME as TAG
          from
          (
            select bc.BOOK_ID, bc.TITLE, bc.BASE_URI, bc.ASPECT_RATIO, bc.NUM_PAGES, bc.COUNTRY, cv.CHARACTER_VOICE_ID, cv.NAME as CHARACTER_VOICE
            from
            (
              select b.BOOK_ID, b.TITLE, b.BASE_URI, b.ASPECT_RATIO, COUNT(bp.PAGE_NUMBER) as NUM_PAGES, c.NAME as COUNTRY
              from BOOKS b
              left join COUNTRIES c on (b.COUNTRY_ID = c.COUNTRY_ID)
              left join BOOK_PAGES bp on (b.BOOK_ID=bp.BOOK_ID) group by b.BOOK_ID, b.TITLE, b.BASE_URI, b.ASPECT_RATIO, c.NAME
            ) bc
            left join BOOK_CHARACTER_VOICES bcvs on (bc.BOOK_ID = bcvs.BOOK_ID)
            left join CHARACTER_VOICES cv on (cv.CHARACTER_VOICE_ID = bcvs.CHARACTER_VOICE_ID)
          ) bcv
          left join BOOK_TAGS bt on (bcv.BOOK_ID = bt.BOOK_ID)
          left join TAGS t on (t.TAG_ID = bt.TAG_ID)
        ) bct
        left join LANGUAGE_BOOKS lb on (bct.BOOK_ID = lb.BOOK_ID)
        left join LANGUAGES l on (l.LANGUAGE_ID = lb.LANGUAGE_ID)
      """.as(Macro.indexedParser[RawBook] *)
      val squashLanguages = squash(
        raw,
        (a: RawBook) => (
            a.id, a.title, a.baseURI, a.aspectRatio, a.numPages,
            a.country, a.characterVoiceID, a.characterVoice, a.tagID, a.tag),
        (a: RawBook) => Language(a.languageID, a.languageName),
        (a: (Int, String, String, Double, Int, String, Option[Int], Option[String], Int, String),
         b: List[Language]) => (a._1, a._2, a._3, a._4, a._5, a._6, a._7, a._8, a._9, a._10, b.toSet)
      )
      val squashTags = squash(
        squashLanguages,
        (a: (Int, String, String, Double, Int, String, Option[Int], Option[String], Int, String, Set[Language])) => 
          (a._1, a._2, a._3, a._4, a._5, a._6, a._7, a._8, a._11),
        (a: (Int, String, String, Double, Int, String, Option[Int], Option[String], Int, String, Set[Language])) => 
          Tag(a._9, a._10),
        (b: (Int, String, String, Double, Int, String, Option[Int], Option[String], Set[Language]),
         c: List[Tag]) => (b._1, b._2, b._3, b._4, b._5, b._6, b._7, b._8, c.toSet, b._9)
      )
      val squashCharacterVoices = squash(
         squashTags,
         (a: (Int, String, String, Double, Int, String, Option[Int], Option[String], Set[Tag], Set[Language])) => 
           (a._1, a._2, a._3, a._4, a._5, a._6, a._9, a._10),
         (a: (Int, String, String, Double, Int, String, Option[Int], Option[String], Set[Tag], Set[Language])) => {
           a._7 match {
             case Some(characterVoiceId) => Some(CharacterVoice(characterVoiceId, a._8.get))
             case None => None
           }
         },
         (b: (Int, String, String, Double, Int, String, Set[Tag], Set[Language]),
          c: List[Option[CharacterVoice]]) => 
            Book(b._1, b._2, b._3, b._4, b._5, b._6, sequence(c), b._7, b._8)
      )
      
      Ok(squashCharacterVoices)
    }
  }
  post("/text"){
    val trOpt = parsedBody.extractOpt[TextRequest]
    trOpt match {
      case Some(tr) => {
        DB.conn{implicit c =>
          val p = SQL"""
            select bp.PAGE_NUMBER, t.TEXT
            from 
            (
              select BOOK_PAGE_ID, PAGE_NUMBER from BOOK_PAGES where BOOK_ID=#${tr.bookID}
            ) bp
            left join 
            (
              select BOOK_PAGE_ID, TEXT from BOOK_PAGE_TEXT where LANGUAGE_BOOK_ID=
              (
                select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where BOOK_ID=#${tr.bookID} and LANGUAGE_ID=#${tr.languageID}
              )
            ) t
            on (t.BOOK_PAGE_ID=bp.BOOK_PAGE_ID)
          """
          val pParsed = p.as(Macro.indexedParser[PageText] *)
          Ok(pParsed)
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book ID and language ID"))
    }
  }
  
  //creating
  post("/create"){
    val bOpt = parsedBody.extractOpt[CreateBookRequest]
    bOpt match {
      case Some(b) => {
        DB.conn{implicit c =>
          SQL("""
            insert into BOOKS (TITLE, BASE_URI, ASPECT_RATIO, COUNTRY_ID)
            values (
              {title},
              {baseURI},
              {aspectRatio},
              (select COUNTRY_ID from COUNTRIES where COUNTRIES.NAME={country})
            )
          """)
          .on('title -> b.title, 'baseURI -> b.baseURI, 'aspectRatio -> b.aspectRatio, 'country -> b.country)
          .executeInsert() match {
            case Some(bid) => {
              b.characterVoices.foreach(
                characterVoice => {
                  SQL("""
                    insert into BOOK_CHARACTER_VOICES (BOOK_ID, CHARACTER_VOICE_ID)
                    values (
                      {bid},
                      (select CHARACTER_VOICE_ID from CHARACTER_VOICES where CHARACTER_VOICE.NAME={characterVoices})
                    )
                  """).on('bid -> bid, 'characterVoice -> characterVoice).executeInsert()
                }    
              )
              b.tags.foreach(
                tag => {
                  SQL("""
                    insert into BOOK_TAGS (BOOK_ID, TAG_ID)
                    values (
                      {bid},
                      (select TAG_ID from TAGS where TAGS.NAME={tag})
                    )
                  """).on('bid -> bid, 'tag -> tag).executeInsert()
                }
              )
              b.languages.foreach(
                language => {
                  SQL("""
                    insert into LANGUAGE_BOOKS (BOOK_ID, LANGUAGE_ID)
                    values (
                      {bid},
                      (select LANGUAGE_ID from LANGUAGES where LANGUAGES.NAME={language})
                    )
                  """).on('bid -> bid, 'language -> language).executeInsert()
                }    
              )
              Ok(bid)
            }
            case None => BadRequest(ErrorMessage("Unable to add book to database"))
          }
          
        }
      }
      case None => BadRequest(ErrorMessage("Unable to parse book"))
    }
  }
  post("/addTextForPages"){
    val aOpt = parsedBody.extractOpt[AddTextRequest]
    aOpt match {
      case Some(a) => {
        DB.conn{implicit c =>
          //n is zero indexed
          for((p,n) <- a.text.view.zipWithIndex) {
            SQL("""
              insert into BOOK_PAGE_TEXT (
                LANGUAGE_BOOK_ID,
                BOOK_PAGE_ID,
                TEXT
              ) values (
                (
                  select LANGUAGE_BOOK_ID from LANGUAGE_BOOKS where LANGUAGE_BOOKS.BOOK_ID={bookID}
                    AND LANGUAGE_BOOKS.LANGUAGE_ID={languageID}
                ),
                (
                  select BOOK_PAGE_ID from BOOK_PAGES where BOOK_PAGES.BOOK_ID={bookID}
                    AND BOOK_PAGES.PAGE_NUMBER={pageNumber}
                ),
                {text}
              )
            """)
            .on('bookID -> a.bookID, 'languageID -> a.languageID, 'pageNumber -> n, 'text -> p)
            .executeInsert() match{
              case Some(tid) => {
                Ok(tid)
              }
              case None => BadRequest(ErrorMessage("Unable to add page # " + n + " to database"))
            }
          }
        }
      }
      case None => BadRequest(ErrorMessage("Unable to parse bookID, languageID, and text"))
    }
  }
}