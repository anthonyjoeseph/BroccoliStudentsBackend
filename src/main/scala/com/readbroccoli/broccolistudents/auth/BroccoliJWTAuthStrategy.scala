package com.readbroccoli.broccolistudents.auth

import org.scalatra.ScalatraBase
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory

case class JWTClaims(userid:Long)

class BroccoliJWTAuthStrategy(protected override val app: ScalatraBase)
  extends JWTAuthStrategy[JWTClaims](app) {

  
  override protected def getSecret() = "broccolisecretteacupsamsungbrothercuckoo"
  
  override protected def validate(claims:Map[String, String])(implicit request: HttpServletRequest, response: HttpServletResponse): Option[JWTClaims] = {
    val userid = claims.getOrElse("userid", "")
    if(userid.isEmpty()) None
    else Some(JWTClaims(userid.toLong))
  }

}