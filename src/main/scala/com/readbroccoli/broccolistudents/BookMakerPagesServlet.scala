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

case class AddPageRequest(bookID:Int)
case class TextForPageRequest(bookID:Int, pageID:Int, languageID:Int)
case class InsertTextForPageRequest(bookID:Int, pageID:Int, languageID:Int, text:String)
case class OnePageRequest(pageID:Int)
case class InsertMusicForPageRequest(pageID:Int, musicID:Int)
case class DeleteTouchableImageRequest(imageID:Int)
case class addTouchableRequest(
    pageID: Int,
    imageURI: String,
    imageXPercent: Double,
    imageYPercent: Double,
    imageWidthPercent: Double,
    imageHeightPercent: Double,
    soundURI: Option[String])

class BookMakerPagesServlet(db:DBW) extends BroccolistudentsStack with AuthenticationSupport{
  
  case class BookInfo(id:Int, title:String, baseURI:String, aspectRatio:Double, countryID:Int)
  case class Page(id:Int, pageNumber:Int, musicID:Option[Int])
  case class CharacterVoice(id:Int)
  case class Tag(id:Int)
  case class Language(id:Int)
  case class Music(uri:String)
  case class Image(id:Int, uri:String,
                  xPercent:Double, yPercent:Double,
                  widthPercent:Double, heightPercent:Double)
  case class TouchableSound(id:Int, soundURI:String)
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
  
  
  post("/addPage") {
    val prOpt = parsedBody.extractOpt[AddPageRequest]
    prOpt match {
      case Some(pr) => {
        db.run{implicit c =>
          val pageNumber:Int = SQL"""
            select COUNT(BOOK_PAGE_ID) from BOOK_PAGES where BOOK_ID=#${pr.bookID}
          """.as(scalar[Int].single)
          val pageidOpt:Option[Long] = SQL"""
            insert into BOOK_PAGES (BOOK_ID, PAGE_NUMBER) values (
              #${pr.bookID},
              #${pageNumber}
            )
          """.executeInsert()
          pageidOpt match {
            case Some(pageID) => Ok(pageID)
            case None => Forbidden(ErrorMessage("Error inserting page to BOOK_PAGES"))
          }
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book ID"))
    }
  }
  delete("/lastPage"){
    val prOpt = parsedBody.extractOpt[AddPageRequest]
    prOpt match {
      case Some(pr) => {
        db.run{implicit c =>
          val pageNumber:Int = SQL"""
            select COUNT(BOOK_PAGE_ID) from BOOK_PAGES where BOOK_ID=#${pr.bookID}
          """.as(scalar[Int].single)
          val numRowsDeleted:Int = SQL"""
            delete from BOOK_PAGES where BOOK_ID=#${pr.bookID} and PAGE_NUMBER=#${pageNumber - 1}
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
  post("/textForPage"){
    val tpOpt = parsedBody.extractOpt[TextForPageRequest]
    tpOpt match {
      case Some(tp) => {
        db.run{implicit c =>
          val text:Option[String] = SQL"""
            select t.TEXT
            from 
            (
              select BOOK_PAGE_ID, PAGE_NUMBER from BOOK_PAGES where BOOK_ID=#${tp.bookID} and BOOK_PAGE_ID=#${tp.pageID}
            ) bp
            left join 
            (
              select BOOK_PAGE_ID, TEXT from BOOK_PAGE_TEXT where LANGUAGE_BOOK_ID=
              (
                select LANGUAGE_BOOK_ID from BOOK_LANGUAGES where BOOK_ID=#${tp.bookID} and LANGUAGE_ID=#${tp.languageID}
              )
            ) t
            on (t.BOOK_PAGE_ID=bp.BOOK_PAGE_ID)
          """.as(scalar[String].singleOpt)
          Ok(text)
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book ID and language ID"))
    }
  }
  post("/insertTextForPage"){
    val tpOpt = parsedBody.extractOpt[InsertTextForPageRequest]
    tpOpt match {
      case Some(tp) => {
        db.run{implicit c =>
          SQL("""
            insert into BOOK_PAGE_TEXT (BOOK_PAGE_ID, LANGUAGE_BOOK_ID, TEXT) values (
              {pageID},
              (select LANGUAGE_BOOK_ID from BOOK_LANGUAGES where LANGUAGE_ID={languageID} and BOOK_ID={bookID}),
              {text}
            )
          """).on('pageID -> tp.pageID , 'languageID -> tp.languageID , 'bookID -> tp.bookID , 'text -> tp.text ).executeInsert()
          Ok
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book ID, page ID, language ID, and text"))
    }
  }
  post("/insertMusicForPage"){
    val mrOpt = parsedBody.extractOpt[InsertMusicForPageRequest]
    mrOpt match {
      case Some(mr) => {
        db.run{implicit c =>
          SQL("""
            update BOOK_PAGES set MUSIC_ID={musicID} where BOOK_PAGE_ID={pageID}
          """).on('pageID -> mr.pageID, 'musicID -> mr.musicID).executeUpdate()
          Ok
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse page ID and music ID"))
    }
  }
  /*select i.IMAGE_ID, i.URI,
                     i.X_PERCENT, i.Y_PERCENT,
                     i.WIDTH_PERCENT, i.HEIGHT_PERCENT,
                   ta.TOUCHABLE_ANIMATION_ID, ta.RESULTING_IMAGE_ID,
                   tp.TOUCHABLE_PRONUNCIATION_ID, tp.SOUND_URI,
                     tp.IMAGE_URI, tp.TEXT,
                   ts.TOUCHABLE_SOUND_ID, ts.URI
            from IMAGES i
            left join TOUCHABLE_ANIMATIONS ta on (ta.IMAGE_ID=i.IMAGE_ID)
            left join TOUCHABLE_PRONUNCIATIONS tp on (tp.IMAGE_ID=i.IMAGE_ID)
            left join TOUCHABLE_SOUNDS ts on (ts.IMAGE_ID=i.IMAGE_ID)
            left join TOUCHABLE_QUIZZES tq on (tq.IMAGE_ID=i.IMAGE_ID)
            where i.BOOK_PAGE_ID=#${tp.pageID}*/
  post("/imagesForPage"){
    val tpOpt = parsedBody.extractOpt[OnePageRequest]
    tpOpt match {
      case Some(tp) => {
        db.run{implicit c =>
          val parser = for {
            imageID <- int("i.IMAGE_ID")
            imageURI <- str("i.URI")
            xPercent <- double("X_PERCENT")
            yPercent <- double("Y_PERCENT")
            widthPercent <- double("WIDTH_PERCENT")
            heightPercent <- double("HEIGHT_PERCENT")
          } yield (
              Image(imageID, imageURI, xPercent, yPercent, widthPercent, heightPercent)
          )
          val raw = SQL"""
            select i.IMAGE_ID, i.URI,
                     i.X_PERCENT, i.Y_PERCENT,
                     i.WIDTH_PERCENT, i.HEIGHT_PERCENT
            from IMAGES i
            where i.BOOK_PAGE_ID=#${tp.pageID}
          """.as(parser *)
          Ok(raw)
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse page ID and music ID"))
    }
  }
  post("/addImageForPage"){
    val brOpt = parsedBody.extractOpt[addTouchableRequest]
    brOpt match {
      case Some(br) => {
        db.run{implicit c =>
          val imageID = SQL("""
            insert into IMAGES (URI, X_PERCENT, Y_PERCENT, WIDTH_PERCENT, HEIGHT_PERCENT, BOOK_PAGE_ID) values (
              {uri},
              {xPercent},
              {yPercent},
              {widthPercent},
              {heightPercent},
              {bookPageID}
            )
          """).on('uri -> br.imageURI,
              'xPercent -> br.imageXPercent,
              'yPercent -> br.imageYPercent,
              'widthPercent -> br.imageWidthPercent,
              'heightPercent -> br.imageHeightPercent,
              'bookPageID -> br.pageID)
              .executeInsert()
          Ok(imageID)
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse book data"))
    }
  }
  delete("/image"){
    val diOpt = parsedBody.extractOpt[DeleteTouchableImageRequest]
    diOpt match {
      case Some(di) => {
        db.run{implicit c =>
          val numRowsDeleted:Int = SQL"""
            delete from IMAGES where IMAGE_ID=#${di.imageID}
          """.executeUpdate()
          if(numRowsDeleted > 0){
            Ok(numRowsDeleted)
          }else{
            BadRequest(ErrorMessage("No Rows Deleted"))
          }
        }
      }
      case None => BadRequest(ErrorMessage("Unable to parse page ID and image ID"))
    }
  }
}