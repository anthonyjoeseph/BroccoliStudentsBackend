package com.readbroccoli.broccolistudents

import anorm._
import anorm.SqlParser._
import com.readbroccoli.broccolistudents.auth.AuthenticationSupport
import com.readbroccoli.broccolistudents.utils.DB
import org.scalatra.{Ok, BadRequest, Conflict, InternalServerError}
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.mysql.jdbc.MysqlDataTruncation

/**
 * @author anthonygabriele
 */

case class UserCredentials(name:String, password:String)

class UserServlet extends BroccolistudentsStack{
  post("/verify"){
    val possibleUserOpt = parsedBody.extractOpt[UserCredentials]
    possibleUserOpt match {
      case Some(possibleUser) => {
        DB.conn{implicit c =>
          val checkUserRaw = SQL"""
            select USER_ID from USERS where name=${possibleUser.name} and password=${possibleUser.password}
          """
          val checkUser = checkUserRaw.as(scalar[Int].singleOpt)
          val returnval = Map("user_exists" -> checkUser.isDefined)
          returnval
        }
      }
      case None => BadRequest("Unable to parse username and password")
    }
  }
  
  post("/create") {
    val newUserOpt = parsedBody.extractOpt[UserCredentials]
    newUserOpt match {
      case Some(newUser) => {
        DB.conn{implicit c =>
          try{
            val addUser = SQL"""
              insert into USERS (NAME, PASSWORD) VALUES ( ${newUser.name}, ${newUser.password} )
            """.executeInsert()
            Ok(Map("new_user_id" -> addUser))
          } catch {
            case e:MySQLIntegrityConstraintViolationException => Conflict("Username already exists")
            case e:MysqlDataTruncation => BadRequest("Username too long")
            case e:Exception => InternalServerError(e)
          }
        }
      }
      case None => BadRequest("Unable to parse username and password")
    }
    
  }
}