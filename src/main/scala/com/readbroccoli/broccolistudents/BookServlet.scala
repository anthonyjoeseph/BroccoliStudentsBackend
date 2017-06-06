package com.broccoli.backend

/**
 * @author anthonygabriele
 */

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

//requests

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
    languages:Set[Language],
    characterVoices:Option[Set[CharacterVoice]], 
    tags:Option[Set[Tag]])
case class BookSingulars(
    id:Int, 
    title:String, 
    baseURI:String, 
    aspectRatio:Double, 
    numPages:Int,
    country:String
)
object BookSingulars {
  val parser: RowParser[BookSingulars] = {
    int("BOOK_ID") ~
    str("TITLE") ~
    str("BASE_URI") ~
    double("ASPECT_RATIO") ~
    int("NUM_PAGES") ~
    str("COUNTRY") map {
      case id ~ title ~ baseURI ~ aspectRatio ~ numPages ~ country => 
        BookSingulars(id, title, baseURI, aspectRatio, numPages, country)
    }
  }
}
case class Language(id:Int, name:String)
object Language{
  val parser: RowParser[Language] = {
      int("LANGUAGE_ID") ~
      str("LANGUAGE_NAME") map {
        case id ~ name => Language(id, name)
      }
  }
}
case class CharacterVoice(id:Int, name:String)
object CharacterVoice{
  val parser: RowParser[Option[CharacterVoice]] = {
      (int("CHARACTER_VOICE_ID") ?) ~
      (str("CHARACTER_VOICE") ?) map {
        case idOpt ~ nameOpt => idOpt match {
          case Some(id) => Some(CharacterVoice(id, nameOpt.get))
          case None => None
        } 
      }
  }
}
case class Tag(id:Int, name:String)
object Tag{
  val parser: RowParser[Option[Tag]] = {
      (int("TAG_ID") ?) ~
      (str("TAG") ?) map {
        case idOpt ~ nameOpt => idOpt match {
          case Some(id) => Some(Tag(id, nameOpt.get))
          case None => None
        } 
      }
  }
}
case class RawBook(
    bookSingulars:BookSingulars,
    language:Language,
    characterVoiceOpt:Option[CharacterVoice],
    tagOpt:Option[Tag]
    )
object RawBook{
  val parser: RowParser[RawBook] = {
    BookSingulars.parser ~
    Language.parser ~
    CharacterVoice.parser ~
    Tag.parser map {
      case bookSingulars ~ language ~ characterVoice ~ tag =>
         RawBook(bookSingulars, language, characterVoice, tag)
    }
  }
}
case class PageText(pageNumber:Int, text:String)

class BookServlet(db:DBW) extends BroccolistudentsStack with AuthenticationSupport {
  def squash[A, B, C, T]( data:List[A], groupBy:A => B, extract: A => T, convert:(B, List[T]) => C): List[C] = {
    data.groupBy[B]( g => groupBy(g) ).map{
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
  get("/env"){
    /*val config = ConfigFactory.load()
    val stringValue = config.getString("appConfig.keyString")
    val boolValue = config.getBoolean("appConfig.keyBool")
    val intValue = config.getInt("appConfig.keyInt")*/
    Ok("env");
  }
  //accessing
  get("/allMeta"){
    db.run{implicit c =>
      val raw:List[RawBook] = SQL"""
        select b.BOOK_ID, b.TITLE, b.BASE_URI,
            b.ASPECT_RATIO, COUNT(bp.PAGE_NUMBER) as NUM_PAGES,
            c.NAME as COUNTRY,
            cv.CHARACTER_VOICE_ID, cv.NAME as CHARACTER_VOICE,
            t.TAG_ID, t.NAME as TAG,
            l.LANGUAGE_ID, l.NAME as LANGUAGE_NAME
        from BOOKS b
        left join COUNTRIES c on (b.COUNTRY_ID = c.COUNTRY_ID)
        left join BOOK_PAGES bp on (b.BOOK_ID=bp.BOOK_ID)/* group by b.BOOK_ID, b.TITLE, b.BASE_URI, b.ASPECT_RATIO, c.NAME*/
        left join BOOK_CHARACTER_VOICES bcvs on (b.BOOK_ID = bcvs.BOOK_ID)
        left join CHARACTER_VOICES cv on (cv.CHARACTER_VOICE_ID = bcvs.CHARACTER_VOICE_ID)
        left join BOOK_TAGS bt on (b.BOOK_ID = bt.BOOK_ID)
        left join TAGS t on (t.TAG_ID = bt.TAG_ID)
        left join BOOK_LANGUAGES lb on (b.BOOK_ID = lb.BOOK_ID)
        left join LANGUAGES l on (l.LANGUAGE_ID = lb.LANGUAGE_ID)
        group by b.BOOK_ID, b.TITLE, b.BASE_URI, b.ASPECT_RATIO, c.NAME, 
        cv.CHARACTER_VOICE_ID, cv.NAME, t.TAG_ID, t.NAME, l.LANGUAGE_ID, 
        l.NAME
      """.as(RawBook.parser *)
      
      val books = raw.groupBy[BookSingulars](_.bookSingulars) map {
        case (k, v) => {
          val languages = v map (_.language) toSet
          val characterVoices = sequence( v map (_.characterVoiceOpt) )
          val tags = sequence( v map (_.tagOpt) )
          Book(k.id, k.title, k.baseURI, k.aspectRatio, k.numPages, k.country,
              languages, characterVoices, tags)
        }
      }
      Ok(books)
    }
  }
  post("/text"){
    val trOpt = parsedBody.extractOpt[TextRequest]
    trOpt match {
      case Some(tr) => {
        db.run{implicit c =>
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
                select LANGUAGE_BOOK_ID from BOOK_LANGUAGES where BOOK_ID=#${tr.bookID} and LANGUAGE_ID=#${tr.languageID}
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
        db.run{implicit c =>
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
        db.run{implicit c =>
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