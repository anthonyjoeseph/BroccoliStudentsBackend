package com.readbroccoli.broccolistudents

import anorm._
import anorm.SqlParser._
import com.readbroccoli.broccolistudents.auth.AuthenticationSupport
import com.readbroccoli.broccolistudents.utils.DB

/**
 * @author anthonygabriele
 */

case class BookTitle(title:String)

class DataServlet extends BroccolistudentsStack with AuthenticationSupport{
  post("/") {
    val user = scentry.authenticate("UserPassword")
    DB.conn{implicit c =>
      val bgs = SQL"""
        select TITLE from BOOKS
      """.as(Macro.indexedParser[BookTitle] *)
      bgs
    }
  }
}