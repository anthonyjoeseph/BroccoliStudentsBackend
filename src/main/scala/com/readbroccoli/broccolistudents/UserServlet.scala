package com.broccoli.backend

import anorm._
import anorm.SqlParser._
import com.broccoli.backend.auth.{AuthenticationSupport, JWTClaims}
import org.blinkmob.scalatraseed.{DB, DBW}
import org.scalatra.{Ok, Forbidden, Conflict, InternalServerError}
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.mysql.jdbc.MysqlDataTruncation
import authentikat.jwt._
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
 * @author anthonygabriele
 */

case class ErrorMessage(error:String)
case class UserCredentials(name:String, password:String)
case class JWTResp(jwt:String)

class UserServlet(db:DBW) extends BroccolistudentsStack with AuthenticationSupport{
  post("/verify"){
    val possibleUserOpt = parsedBody.extractOpt[UserCredentials]
    possibleUserOpt match {
      case Some(possibleUser) => {
        db.run{implicit c =>
          val userIDOpt = SQL"""
            select USER_ID from USERS where name=${possibleUser.name} and password=${possibleUser.password}
          """.as(scalar[Int].singleOpt)
          userIDOpt match {
            case Some(userID) => {
              val header = JwtHeader("HS256")
              val claimsSet = JwtClaimsSet(Extraction.decompose(JWTClaims(userID)))
              Ok(JWTResp(JsonWebToken(header, claimsSet, getSecret)))
            }
            case None => Forbidden(ErrorMessage("Username/password invalid"))
          }
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse username and password"))
    }
  }
  
  post("/create") {
    val newUserOpt = parsedBody.extractOpt[UserCredentials]
    newUserOpt match {
      case Some(newUser) => {
        db.run{implicit c =>
          try{
            val addUser = SQL"""
              insert into USERS (NAME, PASSWORD) VALUES ( ${newUser.name}, ${newUser.password} )
            """.executeInsert()
            Ok(Map("new_user_id" -> addUser))
          } catch {
            case e:MySQLIntegrityConstraintViolationException => Forbidden(ErrorMessage("Username already exists"))
            case e:MysqlDataTruncation => Forbidden(ErrorMessage("Username too long"))
            case e:Exception => InternalServerError(e)
          }
        }
      }
      case None => Forbidden(ErrorMessage("Unable to parse username and password"))
    }
    
  }
}