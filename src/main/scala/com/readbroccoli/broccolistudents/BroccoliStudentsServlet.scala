package com.readbroccoli.broccolistudents

import org.scalatra._
import anorm._
import anorm.SqlParser._
import com.readbroccoli.broccolistudents.utils.DB;
//import play.api.db.DB

case class BookTitle(title:String)

class BroccoliStudentsServlet extends BroccolistudentsStack {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
    
  }

  get("/new"){  
    DB.conn{implicit c =>
      val bgs = SQL"""
        select TITLE from BOOKS
      """.as(Macro.indexedParser[BookTitle] *)
      bgs
    }
  }
}
