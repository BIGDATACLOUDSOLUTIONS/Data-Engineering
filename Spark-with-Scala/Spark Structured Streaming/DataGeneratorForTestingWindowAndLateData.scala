import scala.util.Random
import scala.io._

object PlayGround {

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
      val cityNameList=Source.fromFile("C:\\Users\\JKE1KOR\\IdeaProjects\\Codality\\src\\main\\resources\\1000CityNames.csv").getLines().toList
      cityNameList(random.nextInt(cityNameList.length))
    }

    val finalString = (1 to 50).map { x =>
      val eventTime= s"$getRandomYear-$getRandomMonth-$getRandomDays $getRandomHrs:$getRandomMinutes:$getRandomMiliseconds"
      val cityName=getRandomCityName
      val pollutionIndex=getRandomPollutionIndex
      s"""{eventTime:$eventTime,cityName:$cityName,pollutionIndex:$pollutionIndex}"""
    }
    println(finalString)
  }

}
