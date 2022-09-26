package com.datagenerator.simpleJson.PollutionIndex

import scala.util.Random
import scala.io._
import org.apache.log4j.Logger

object generatePollutionIndexData {

  val ipaddress="localhost"
  val bootStrapServerIP = s"$ipaddress:9092,$ipaddress:9093,$ipaddress:9094"

  def main(args: Array[String]): Unit = {

    val random = new Random


    //2019-01-29 12:02:00
    def getRandomYear: String = {
      val year = 2019 to 2020
      year(random.nextInt(year.length)).toString
    }

    def getRandomMonth: String = {
      val mon = 1 to 12
      "%02d".format(mon(random.nextInt(mon.length))).toString
    }

    def getRandomDays: String = {
      val days = 1 to 30
      "%02d".format(days(random.nextInt(days.length))).toString
    }

    def getRandomHrs: String = {
      val hrs = 1 to 24
      "%02d".format(hrs(random.nextInt(hrs.length))).toString

    }

    def getRandomMinutes: String = {
      val min = 1 to 60
      "%02d".format(min(random.nextInt(min.length))).toString
    }

    def getRandomSeconds: String = {
      val seconds = 1 to 60
      "%02d".format(seconds(random.nextInt(seconds.length))).toString
    }

    def getRandomMiliseconds: String = {
      val miliseconds = 1 to 60
      "%02d".format(miliseconds(random.nextInt(miliseconds.length))).toString
    }

    def getRandomPollutionIndex:String={
      val pollutionIndex = 10 to 99
      pollutionIndex(random.nextInt(pollutionIndex.length)).toString
    }

    def getRandomCityName:String={
      val cityNameList=Source.fromFile("C:\\Users\\RAJES\\IdeaProjects\\Kafka\\KafkaDataGenerator\\src\\main\\Resources\\Data\\1000CityNames.csv").getLines().toList
      cityNameList(random.nextInt(cityNameList.length))
    }

    val logger = Logger.getLogger(getClass.getName)

    val topic = "pollution_index"
    val CLIENT_ID = "pollution_index"

    logger.info(s"Starting Data Generator for topic ${topic}...")
    logger.info(s"kafka.bootstrap.servers: ${bootStrapServerIP}")
    logger.info(s"CLIENT_ID: ${CLIENT_ID}")

    (1 to 50).map { x =>
      val eventTime= s"$getRandomYear-$getRandomMonth-$getRandomDays $getRandomHrs:$getRandomMinutes:$getRandomMiliseconds"
      val cityName=getRandomCityName
      val pollutionIndex=getRandomPollutionIndex
      val payload = s"""{eventTime:$eventTime,cityName:$cityName,pollutionIndex:$pollutionIndex}"""
      confluentProducer.send(topic, payload)
      Thread.sleep(1000)
    }

  }

}
