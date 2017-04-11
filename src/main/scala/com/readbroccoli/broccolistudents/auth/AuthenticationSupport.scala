package com.readbroccoli.broccolistudents.auth

import org.scalatra.auth.strategy.{BasicAuthStrategy, BasicAuthSupport}
import org.scalatra.auth.{ScentrySupport, ScentryConfig}
import org.scalatra.{ScalatraBase}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}

trait AuthenticationSupport extends ScalatraBase with ScentrySupport[JWTClaims] with JWTAuthSupport[JWTClaims] {
  self: ScalatraBase =>

  protected val scentryConfig = (new ScentryConfig {}).asInstanceOf[ScentryConfiguration]
  private implicit val jsonFormats: Formats = DefaultFormats

  protected def fromSession = {
    case claimstr: String => parse(claimstr).extract[JWTClaims]
  }

  protected def toSession = {
    case claim: JWTClaims => write(user)
  }
  
  

  override protected def configureScentry = {
    scentry.unauthenticated {
      scentry.strategies("Bearer").unauthenticated()
    }
  }

  override protected def registerAuthStrategies = {
    scentry.register("Bearer", app => new BroccoliJWTAuthStrategy(app))
  }
  
}