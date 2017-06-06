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

case class AddMusicRequest(uri:String)

class BookMakerMusicServlet(db:DBW) extends BroccolistudentsStack with AuthenticationSupport{
  
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
  
  delete("/music"){
    val dmOpt = parsedBody.extractOpt[AddPageRequest]
    dmOpt match {
      case Some(dm) => {
        db.run{implicit c =>
          val numRowsDeleted:Int = SQL"""
            delete from MUSIC where MUSIC_ID=#${dm.bookID}
          """.executeUpdate()
          if(numRowsDeleted > 0){
            Ok(numRowsDeleted)
          }else{
            BadRequest(ErrorMessage("No Rows Deleted"))
          }
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse music ID"))
    }
  }
  get("/allMusic"){
    db.run{implicit c =>
      val music = SQL"""
        select MUSIC_ID, URI from MUSIC
      """.as(int("MUSIC_ID") ~ str("URI") map (SqlParser.flatten) *)
      Ok(music)
    }
  }
  post("/addMusic"){
    val amOpt = parsedBody.extractOpt[AddMusicRequest]
    amOpt match {
      case Some(am) => {
        db.run{implicit c =>
          SQL("""
            insert into MUSIC (URI) values ({uri});
          """).on('uri -> am.uri).executeInsert()
          Ok
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse uri"))
    }
  }
}