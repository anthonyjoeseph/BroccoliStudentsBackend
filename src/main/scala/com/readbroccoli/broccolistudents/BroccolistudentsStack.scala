package com.broccoli.backend

import org.scalatra._
import javax.servlet.http.HttpServletRequest
import collection.mutable
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

trait BroccolistudentsStack extends ScalatraServlet with JacksonJsonSupport {
  
  protected implicit val jsonFormats: Formats = DefaultFormats.withBigDecimal
  
  def getSecret = "broccolisecretteacupsamsungbrothercuckoo"
  
  before() {
    contentType = formats("json")
  }

}
