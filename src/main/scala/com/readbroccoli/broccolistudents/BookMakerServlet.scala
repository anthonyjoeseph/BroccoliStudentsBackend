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
import better.files._
import better.files.File._
import java.nio.file.attribute.PosixFilePermission


/**
 * @author anthonygabriele
 */

case class AddBookRequest(
    title:String, 
    baseURI:String,
    aspectRatio:Double,
    countryID:Int, 
    languageIDs:List[Int],
    voiceIDs:List[Int],
    tagIDs:List[Int])
case class EditBookRequest(
    bookID:Int,
    title:String, 
    baseURI:String,
    aspectRatio:Double,
    countryID:Int, 
    addLanguageIDs:List[Int],
    removeLanguageIDs:List[Int],
    addVoiceIDs:List[Int],
    removeVoiceIDs:List[Int],
    addTagIDs:List[Int],
    removeTagIDs:List[Int])

class BookMakerServlet(db:DBW) extends BroccolistudentsStack with AuthenticationSupport{
  
  case class BookInfo(id:Int, title:String, baseURI:String, aspectRatio:Double, countryID:Int)
  case class Page(id:Int, pageNumber:Int, musicID:Option[Int])
  case class CharacterVoice(id:Int)
  case class Tag(id:Int)
  case class Language(id:Int)
  case class Music(uri:String)
  case class Book(bookInfo:BookInfo, pages:Option[Set[Page]], characterVoices:Option[Set[CharacterVoice]], tags:Option[Set[Tag]], languages:Set[Language])
  
  def squash[A, B, C, T]( data:List[A], groupBy:A => B, extract: A => T, convert:(B, List[T]) => C): List[C] = {
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
  
  post("/addBook") {
    val brOpt = parsedBody.extractOpt[AddBookRequest]
    brOpt match {
      case Some(br) => {
        db.run{implicit c =>
          val bookidOpt:Option[Long] = SQL"""
            insert into BOOKS (TITLE, BASE_URI, ASPECT_RATIO, COUNTRY_ID) values (
              "#${br.title}",
              "#${br.baseURI}",
              "#${br.aspectRatio}",
              "#${br.countryID}"
            )
          """.executeInsert()
          bookidOpt match {
            case Some(bookID) => {
              br.languageIDs.foreach(
                (languageID) => {
                  SQL"""
                    insert into BOOK_LANGUAGES (BOOK_ID, LANGUAGE_ID) values (
                      #${bookID},
                      #${languageID}
                    )
                  """.executeInsert()
                }
              )
              br.voiceIDs.foreach(
                (voiceID) => {
                  SQL"""
                    insert into BOOK_CHARACTER_VOICES (BOOK_ID, CHARACTER_VOICE_ID) values (
                      #${bookID},
                      #${voiceID}
                    )
                  """.executeInsert()
                }    
              )
              br.tagIDs.foreach(
                (tagID) => {
                  SQL"""
                    insert into BOOK_TAGS (BOOK_ID, TAG_ID) values (
                      #${bookID},
                      #${tagID}
                    )
                  """.executeInsert()
                }    
              )
              Ok(bookID)
            }
            case None => Forbidden(ErrorMessage("Unable to insert book into BOOKS"))
          }
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book data"))
    }
  }
  post("/editBook") {
    val brOpt = parsedBody.extractOpt[EditBookRequest]
    brOpt match {
      case Some(br) => {
        db.run{implicit c =>
          SQL("""
                 update BOOKS set
                 TITLE={title},
                 BASE_URI={baseURI},
                 ASPECT_RATIO={aspectRatio},
                 COUNTRY_ID={countryID}
                 where BOOK_ID={bookID}
          """).on('title -> br.title, 'baseURI -> br.baseURI,
                  'aspectRatio -> br.aspectRatio,
                  'countryID -> br.countryID,
                  'bookID -> br.bookID)
                  .executeUpdate()
          br.addLanguageIDs.foreach(
            (languageID) => {
              SQL("""
                insert into BOOK_LANGUAGES (LANGUAGE_ID, BOOK_ID) values (
                  {languageID},
                  {bookID}
                )
              """).on('languageID -> languageID, 'bookID -> br.bookID).executeInsert()
            }
          )
          br.removeLanguageIDs.foreach(
            (languageID) => {
              SQL("""
                delete from BOOK_LANGUAGES where
                  LANGUAGE_ID={languageID} and
                  BOOK_ID={bookID}
              """).on('languageID -> languageID, 'bookID -> br.bookID).executeUpdate()
            }
          )
          br.addVoiceIDs.foreach(
            (voiceID) => {
              SQL("""
                insert into BOOK_CHARACTER_VOICES (CHARACTER_VOICE_ID, BOOK_ID) values (
                  {voiceID},
                  {bookID}
                )
              """).on('voiceID -> voiceID, 'bookID -> br.bookID).executeInsert()
            }
          )
          br.removeVoiceIDs.foreach(
            (voiceID) => {
              SQL("""
                delete from BOOK_CHARACTER_VOICES where
                  CHARACTER_VOICE_ID={voiceID} and
                  BOOK_ID={bookID}
              """).on('voiceID -> voiceID, 'bookID -> br.bookID).executeInsert()
            } 
          )
          
          br.addTagIDs.foreach(
            (tagID) => {
              SQL("""
                insert into BOOK_TAGS (TAG_ID, BOOK_ID) values (
                  {tagID},
                  {bookID}
                )
              """).on('tagID -> tagID, 'bookID -> br.bookID).executeInsert()
            }
          )
          br.removeTagIDs.foreach(
            (tagID) => {
              SQL("""
                delete from BOOK_TAGS where
                  TAG_ID={tagID} and
                  BOOK_ID={bookID}
              """).on('tagID -> tagID, 'bookID -> br.bookID).executeUpdate()
            }    
          )
          Ok(br.bookID)
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book data"))
    }
  }
  delete("/book"){
    val prOpt = parsedBody.extractOpt[AddPageRequest]
    prOpt match {
      case Some(pr) => {
        db.run{implicit c =>
          val numRowsDeleted:Int = SQL"""
            delete from BOOKS where BOOK_ID=#${pr.bookID}
          """.executeUpdate()
          if(numRowsDeleted > 0){
            Ok(numRowsDeleted)
          }else{
            BadRequest(ErrorMessage("No Rows Deleted"))
          }
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book ID"))
    }
  }
  get("/tables"){
    db.run{implicit c =>
      val countries:List[(Int, String)] = SQL"""
        select COUNTRY_ID, NAME from COUNTRIES
      """.as(int("COUNTRY_ID") ~ str("NAME") map (SqlParser.flatten) *)
      val languages:List[(Int, String)] = SQL"""
        select LANGUAGE_ID, NAME from LANGUAGES
      """.as(int("LANGUAGE_ID") ~ str("NAME") map (SqlParser.flatten) *)
      val characterVoices:List[(Int, String)] = SQL"""
        select CHARACTER_VOICE_ID, NAME from CHARACTER_VOICES
      """.as(int("CHARACTER_VOICE_ID") ~ str("NAME") map (SqlParser.flatten) *)
      val tags:List[(Int, String)] = SQL"""
        select TAG_ID, NAME from TAGS
      """.as(int("TAG_ID") ~ str("NAME") map (SqlParser.flatten) *)
      Ok(Map("countries" -> countries, "languages" -> languages, "characterVoices" -> characterVoices, "tags" -> tags))
    }
  }
  get("/allBooks") {
    //ids, titles, pageIDs, pageNumbers, languageIDs, languages
    db.run{implicit c =>
      val parser = for {
        id <- int("BOOK_ID")
        title <- str("TITLE")
        baseURI <- str("BASE_URI")
        aspectRatio <- double("ASPECT_RATIO")
        countryID <- int("COUNTRY_ID")
        bookPageIDOpt <- (int("BOOK_PAGE_ID") ?)
        bookPageNumber <- (int("PAGE_NUMBER") ?)
        bookPageMusicID <- (int("MUSIC_ID") ?)
        characterVoiceIDOpt <- (int("CHARACTER_VOICE_ID") ?)
        tagIDOpt <- (int("TAG_ID") ?)
        languageID <- int("LANGUAGE_ID")
      } yield (
          BookInfo(id, title, baseURI, aspectRatio, countryID),
          bookPageIDOpt match { case Some(bookPageID) => Some(Page(bookPageID, bookPageNumber.get, bookPageMusicID)) case None => None },
          characterVoiceIDOpt match { case Some(characterVoiceID) => Some(CharacterVoice(characterVoiceID)) case None => None },
          tagIDOpt match { case Some(tagID) => Some(Tag(tagID)) case None => None },
          Language(languageID)
      )
      val raw = SQL"""
        select b.BOOK_ID, b.TITLE, b.BASE_URI, b.ASPECT_RATIO, c.COUNTRY_ID,
              bp.BOOK_PAGE_ID, bp.PAGE_NUMBER, bp.MUSIC_ID,
              cv.CHARACTER_VOICE_ID,
              t.TAG_ID,
              l.LANGUAGE_ID
          from BOOKS b
          left join COUNTRIES c on (b.COUNTRY_ID = c.COUNTRY_ID)
          left join BOOK_PAGES bp on (b.BOOK_ID=bp.BOOK_ID)
          left join BOOK_CHARACTER_VOICES bcvs on (b.BOOK_ID = bcvs.BOOK_ID)
          left join CHARACTER_VOICES cv on (cv.CHARACTER_VOICE_ID = bcvs.CHARACTER_VOICE_ID)
          left join BOOK_TAGS bt on (b.BOOK_ID = bt.BOOK_ID)
          left join TAGS t on (t.TAG_ID = bt.TAG_ID)
          left join BOOK_LANGUAGES lb on (b.BOOK_ID = lb.BOOK_ID)
          left join LANGUAGES l on (l.LANGUAGE_ID = lb.LANGUAGE_ID)
      """.as(parser.*)
      val squashLanguages = squash[
        (BookInfo, Option[Page], Option[CharacterVoice], Option[Tag], Language),
        (BookInfo, Option[Page], Option[CharacterVoice], Option[Tag]),
        (BookInfo, Option[Page], Option[CharacterVoice], Option[Tag], Set[Language]),
        Language
      ](
        raw,
        (a) => (a._1, a._2, a._3, a._4),
        (a) => a._5,
        (b, c) => (b._1, b._2, b._3, b._4, c.toSet)
      )
      val squashTags = squash[
        (BookInfo, Option[Page], Option[CharacterVoice], Option[Tag], Set[Language]),
        (BookInfo, Option[Page], Option[CharacterVoice], Set[Language]),
        (BookInfo, Option[Page], Option[CharacterVoice], Option[Set[Tag]], Set[Language]),
        Option[Tag]
      ](
        squashLanguages,
        (a) => (a._1, a._2, a._3, a._5),
        (a) => a._4,
        (b, c) => (b._1, b._2, b._3, sequence(c), b._4)
      )
     val squashCharacterVoices = squash[
       (BookInfo, Option[Page], Option[CharacterVoice], Option[Set[Tag]], Set[Language]),
       (BookInfo, Option[Page], Option[Set[Tag]], Set[Language]),
       (BookInfo, Option[Page], Option[Set[CharacterVoice]], Option[Set[Tag]], Set[Language]),
       Option[CharacterVoice]
     ](
         squashTags,
         (a) => (a._1, a._2, a._4, a._5),
         (a) => a._3,
         (b, c) => (b._1, b._2, sequence(c), b._3, b._4)
      )
      val squashPageIDs = squash[
        (BookInfo, Option[Page], Option[Set[CharacterVoice]], Option[Set[Tag]], Set[Language]),
        (BookInfo, Option[Set[CharacterVoice]], Option[Set[Tag]], Set[Language]),
        Book,
        Option[Page]
      ](
         squashCharacterVoices,
         (a) => (a._1, a._3, a._4, a._5),
         (a) => a._2,
         (b, c) => Book(b._1, sequence(c), b._2, b._3, b._4)
      )
      Ok(squashPageIDs)
    }
  }
}