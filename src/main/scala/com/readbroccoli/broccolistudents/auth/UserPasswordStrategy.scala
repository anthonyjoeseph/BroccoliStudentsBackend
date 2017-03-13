package com.readbroccoli.broccolistudents.auth

import anorm._
import anorm.SqlParser._
import org.scalatra.{Unauthorized, ScalatraBase}
import org.scalatra.auth.ScentryStrategy
import org.scalatra.servlet.{RichRequest}
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

import com.readbroccoli.broccolistudents.utils.DB

case class User(id: String)
case class UserCredentials(name:String, password:String)

class UserPasswordStrategy(protected val app: ScalatraBase)(implicit request: HttpServletRequest, response: HttpServletResponse)
  extends ScentryStrategy[User]{

  val logger = LoggerFactory.getLogger(getClass)

  override def name: String = "UserPassword"

  private def username = Option(request.getHeader("name")).getOrElse("")
  private def password = Option(request.getHeader("password")).getOrElse("")


  /***
    * Determine whether the strategy should be run for the current request.
    */
  override def isValid(implicit request: HttpServletRequest) = {
    logger.info("UserPasswordStrategy: determining isValid: " + (username != "" && password != "").toString())
    username != "" && password != ""
  }

  /**
   *  Authenticate using data from SQL server
   */
  override def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse): Option[User] = {
    logger.info("UserPasswordStrategy: attempting authentication")

    DB.conn{implicit c =>
      val dbPassword = SQL"""
        select PASSWORD from USERS where NAME = $username limit 1
      """.as(scalar[String].singleOpt)
      dbPassword match {
        case Some(p) => {
          if (p == password)
            Some(User(username))
          else
            None
        }
        case None => None
      }
    }
  }

  /**
   * What should happen if the user is currently not authenticated?
   */
  override def unauthenticated()(implicit request: HttpServletRequest, response: HttpServletResponse) {
    app halt Unauthorized()
  }

}
