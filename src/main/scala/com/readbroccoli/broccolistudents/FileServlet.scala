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

//small change for git
case class DeletePageImageRequest(baseURI: String, pageNumber: Int)
case class DeleteBookRequest(baseURI: String)

/**
 * @author anthonygabriele
 */
class FileServlet(db:DBW) extends BroccolistudentsStack with AuthenticationSupport{
  
  val rootPath = "/usr/share/nginx/protectedStaticFiles/"
  
  post("/upload") {
    val tmpFileNameOpt = request.header("X-FILE")
    val relativeFilePathOpt = request.header("Destination")
    tmpFileNameOpt match {
      case Some(tmpFileName) => {
        relativeFilePathOpt match{
          case Some(relativeFilePath) => {
            val tmpFile = tmpFileName.toFile
            val destFileName = rootPath + relativeFilePath
            val destFile = destFileName.toFile.createIfNotExists(false, true)
            tmpFile.copyTo(destFile, true)
            tmpFile.delete(false)
            destFile.addPermission(PosixFilePermission.OWNER_READ)
            destFile.addPermission(PosixFilePermission.GROUP_READ)
            destFile.addPermission(PosixFilePermission.OTHERS_READ)
            Ok(destFileName)
          }
          case None => BadRequest("No destination path found")
        }
      }
      case None => BadRequest("No file name found")
    }
  }
  delete("/page") {
    val diOpt = parsedBody.extractOpt[DeletePageImageRequest]
    diOpt match {
      case Some(di) => {
        val pageImagePath = rootPath + di.baseURI + "/pages/" + di.pageNumber + ".jpg"
        val pageImageFile = pageImagePath.toFile
        pageImageFile.delete(false)
        Ok(di.baseURI + " page # " + di.pageNumber + " was deleted")
      }
      case None => BadRequest("Couldn't parse book ID and page number")
    }
  }
  delete("/music"){
    val diOpt = parsedBody.extractOpt[DeleteBookRequest]
    diOpt match {
      case Some(di) => {
        val musicPath = rootPath + "music/" + di.baseURI
        val musicFile = musicPath.toFile
        musicFile.delete(false)
        Ok(di.baseURI + " was deleted")
      }
      case None => BadRequest("Couldn't parse uri")
    }
  }
  delete("/book") {
    val dbOpt = parsedBody.extractOpt[DeleteBookRequest]
    dbOpt match {
      case Some(db) => {
        val pageImagePath = rootPath + db.baseURI
        val pageImageFile = pageImagePath.toFile
        pageImageFile.delete()
        Ok(db.baseURI + " was deleted")
      }
      case None => BadRequest("Couldn't parse book ID and page number")
    }
  }
}