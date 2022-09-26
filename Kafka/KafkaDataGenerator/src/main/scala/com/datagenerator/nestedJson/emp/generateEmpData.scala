package com.datagenerator.nestedJson.emp

import org.apache.log4j.Logger

object generateEmpData {
  val ipaddress="localhost"
  val bootStrapServerIP = s"$ipaddress:9092,$ipaddress:9093,$ipaddress:9094"

  def main(args: Array[String]): Unit = {
    val logger = Logger.getLogger(getClass.getName)

    val topic = "employee"
    val CLIENT_ID = "emp"

    logger.info(s"Starting Data Generator for topic ${topic}...")
    logger.info(s"kafka.bootstrap.servers: ${bootStrapServerIP}")
    logger.info(s"CLIENT_ID: ${CLIENT_ID}")

    while (true) {
      val r = scala.util.Random
      val id = r.nextInt(1000)
      val alphabet = ('a' to 'z') ++ ('A' to 'Z')
      val ename = (1 to 7).map(_ => alphabet(r.nextInt(alphabet.size))).mkString

      val s1 = ename.toLowerCase
      var gender: String = null
      var match1 = ename.charAt(0)
      var match2 = s1.charAt(0)
      if (match1.equals(match2)) {
        gender = "Female"
      } else {
        gender = "Male"
      }

      val salary = r.nextInt(100000)
      val deptNo = r.nextInt(100)

      val payload =
        s"""{"identifier":"Source1","Data":{"empid": $id,"ename": "$ename","sal": $salary,"gender": "$gender","deptNo": $deptNo}}"""
      confluentProducer.send(topic, payload)
      Thread.sleep(1000)
    }
  }
}