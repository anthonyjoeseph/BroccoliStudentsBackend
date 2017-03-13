
package com.readbroccoli.broccolistudents.utils

import java.sql.Connection
import scala.util.control.ControlThrowable
import scala.util.control.NonFatal
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.Properties
import java.io.FileInputStream
import java.io.File
//import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy

object DB {
  val dbproperties = new Properties()
  /*val propsLoc = Option(sys.props("dbProps"))
  if(propsLoc.isDefined){
    dbproperties.load(new FileInputStream(new File(propsLoc.get)))
  }
  else{
    dbproperties.load(getClass.getClassLoader.getResourceAsStream("local_hikari.properties"))
  }*/
  /*val propsLoc = "src/main/resources/local_hikari.properties";
  dbproperties.load(new FileInputStream(new File(propsLoc)));*/
  dbproperties.load(getClass.getClassLoader.getResourceAsStream("local_hikari.properties"))
  
  val hc = new HikariConfig(dbproperties);
  val ds = new HikariDataSource(hc);
  //val ds = new DataSourceSpy(hds);
  
  def conn[A](block: Connection => A): A = {
    val c = ds.getConnection
    try {
      block(c)
    } finally {
      c.close()
    }
  }

  def tx[A](block: Connection => A): A = {
    conn { c =>
      try {
        c.setAutoCommit(false)
        val r = block(c)
        c.commit()
        r
      } catch {
        case e: ControlThrowable =>
          c.commit(); throw e
        case NonFatal(e) => c.rollback(); throw e
      }
    }
  }
}