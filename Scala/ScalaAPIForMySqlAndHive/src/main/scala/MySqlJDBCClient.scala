import java.sql._

import spray.json._
import scala.collection.mutable.ListBuffer


object MySqlJDBCClient extends App with DefaultJsonProtocol {
    case class SchemaTable(dbName: String, tblName: List[String])
    implicit val SchemaTableJson=jsonFormat2(SchemaTable)

  def getHiveSchemaTableList(connection: Connection):String = {
    var SchemaTableList = new ListBuffer[SchemaTable]()

    val stmt = connection.createStatement()
    val rs = stmt.executeQuery("show databases")

    while(rs.next()) {
      val dbName:String  = rs.getString(1)

      val stmt1 = connection.createStatement()
      stmt1.execute(s"use $dbName")

      val tbl_rs = stmt1.executeQuery("show tables")

      var tblBuffer= ListBuffer[String]()
      while(tbl_rs.next()) {
        val tblName:String = tbl_rs.getString(1)
        tblBuffer += tblName

      }
      SchemaTableList += SchemaTable(dbName,tblBuffer.toList)

    }

    connection.close()
    JsArray(SchemaTableList.toList.map(_.toJson).toVector).toJson.toString
  }

  Class.forName("com.mysql.jdbc.Driver")
  val connection = DriverManager.getConnection("jdbc:mysql://sandbox-hdp.hortonworks.com:3306", "root", "hortonworks1")
  println(getHiveSchemaTableList(connection))


}