
import java.sql.SQLException
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.sql.DriverManager


object HiveJDBCClient extends App {
  val driverName = "org.apache.hive.jdbc.HiveDriver"
  Class.forName(driverName)
  val connection: Connection = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "scott", "")
  val stmt = connection.createStatement()
  val tableName = "emp_hiveJDBCClient"
  //stmt.execute("drop table if exists " + tableName)
  //stmt.execute(s"""create table  $tableName(name string, age string) row format delimited fields terminated by ','""")
  var sql = "show tables '" + tableName + "'"
  println("Running: " + sql)
  var res: ResultSet = stmt.executeQuery(sql)
  if (res.next()) {
    println(res.getString(1))
  }
  sql = "describe " + tableName
  println("Running: " + sql)

  res = stmt.executeQuery(sql)
  while (res.next) {
    println(res.getString(1) + "\t" + res.getString(2))
  }

  // select * query
  sql = "select * from " + tableName
  println("Running: " + sql)
  res = stmt.executeQuery(sql)
  while (res.next) {
    println(String.valueOf(res.getString(1)) + "\t" + res.getString(2))
  }

  // regular hive query
  sql = "select count(1) from " + tableName
  System.out.println("Running: " + sql)
  res = stmt.executeQuery(sql)
  while (res.next) {
    println(res.getString(1))
  }
  connection.close()
}

