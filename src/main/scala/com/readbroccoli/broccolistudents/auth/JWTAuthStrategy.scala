package com.broccoli.backend.auth

import org.scalatra.auth.{ ScentrySupport, ScentryStrategy }
import java.util.Locale
import javax.servlet.http.{ HttpServletResponse, HttpServletRequest }
import io.Codec
import org.scalatra.ScalatraBase
import org.scalatra.Unauthorized
import authentikat.jwt.JsonWebToken
import org.slf4j.LoggerFactory


/**
 * Provides a hook for basic JWT support
 */
trait JWTAuthSupport[UserType <: AnyRef] { self: (ScalatraBase with ScentrySupport[UserType]) =>

  private val logger =  LoggerFactory.getLogger(getClass)
  protected def jwtAuth()(implicit request: HttpServletRequest, response: HttpServletResponse) = {
    val baReq = new JWTAuthStrategy.JWTAuthRequest(request)
    if(!(baReq.hasAuth && baReq.isBearerAuth)) {
      logger.info(s"request has auth ${baReq.hasAuth}, auth is Bearer ${baReq.isBearerAuth}")
      response.setHeader("WWW-Authenticate", "Bearer")
      halt(401)
    }
    scentry.authenticate("Bearer")
  }
}

object JWTAuthStrategy {

  private val authorizationKey = "Authorization"
  class JWTAuthRequest(r: HttpServletRequest) {
    private val jwtHeader = Option(r.getHeader(authorizationKey))
    private val jwtToken = jwtHeader.getOrElse("").split(" ") match {
      case Array(x, y, _*) => Some((x, y))
      case _ => None
    }
    def isBearerAuth = (jwtToken.map{case (h,v) => h == "Bearer"}).getOrElse(false)
    def hasAuth = jwtHeader.isDefined
    def getJWTToken = jwtToken.map{case (h, v) => v}.get
    
  }
}
abstract class JWTAuthStrategy[UserType <: AnyRef](protected val app: ScalatraBase)
  extends ScentryStrategy[UserType] {

  import JWTAuthStrategy._

  implicit def request2BasicAuthRequest(r: HttpServletRequest) = new JWTAuthRequest(r)
  private val logger =  LoggerFactory.getLogger(getClass)
  
  private val challengeheader = Map("WWW-Authenticate" -> "Bearer")
  
  private def unauthorized = app halt Unauthorized(headers = challengeheader)

  protected def getSecret():String

  override def isValid(implicit request: HttpServletRequest) = request.hasAuth && request.isBearerAuth
  def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse) ={
    val t = request.getJWTToken
    logger.info(s"auth token was $t")
    if(!JsonWebToken.validate(t, getSecret)) {
      logger.info(s"auth token invalid!")
      unauthorized
    }
    logger.info("token is valid")
    val claims: Option[Map[String, String]] = t match {
        case JsonWebToken(header, claimsSet, signature) =>
          claimsSet.asSimpleMap.toOption
        case x =>unauthorized
    }
    if(!claims.isDefined) unauthorized
    validate(claims.get)
  }
    

  protected def validate(claims:Map[String, String])(implicit request: HttpServletRequest, response: HttpServletResponse): Option[UserType]

  override def unauthenticated()(implicit request: HttpServletRequest, response: HttpServletResponse) = unauthorized

}