package com.readbroccoli.broccolistudents

import anorm._
import anorm.SqlParser._
import com.readbroccoli.broccolistudents.utils.DB
import com.readbroccoli.broccolistudents.auth.AuthenticationSupport

class BroccoliStudentsServlet extends BroccolistudentsStack with AuthenticationSupport{
  
  post("/") {
    val auth = scentry.authenticate("UserPassword")
    DB.conn{implicit c =>
      val bgs = SQL"""
        select TITLE from BOOKS
      """.as(Macro.indexedParser[BookTitle] *)
      bgs
    }
  }
  
}