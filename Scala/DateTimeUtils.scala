//https://www.logicbig.com/how-to/code-snippets/jcode-java-8-date-time-api-localdatetime-isequal.html

import java.time.{Duration, LocalDateTime, Period, ZoneId}
import java.time.format.DateTimeFormatter

object DateTimeUtils {

  def compare2Dates(date1:List[Int],date2:List[Int])={

    val a = LocalDateTime.of(date1.head, date1(1), date1(2), date1(3), date1(4),date1(5))
    val b = LocalDateTime.of(date2.head, date2(1), date2(2), date2(3), date2(4),date2(5))
    println(a.isEqual(b))
    println(a.compareTo(b))
    println(a == b)
  }

  def subtractHoursFromCurrentTimestamp(hoursToSubtract:Int):String={
    val currentTime = LocalDateTime.now()
    val yest= currentTime.minus(Duration.ofHours(hoursToSubtract))

    val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    val yestDateString = yest.format(outputDateFormat)

    yestDateString
  }

  def getDifferenceInHours(fromDate:Array[Int],toDate:Array[Int])={

    val fromDateTime  = LocalDateTime.of(fromDate.head, fromDate(1), fromDate(2), fromDate(3), fromDate(4),fromDate(5))
    val toDateTime  = LocalDateTime.of(toDate.head, toDate(1), toDate(2), toDate(3), toDate(4),toDate(5))

    val period = Period.between(fromDateTime.toLocalDate, toDateTime.toLocalDate)

    period.getDays
  }

  def Redi()={
    val currentDateTime=LocalDateTime.now(ZoneId.of("-05:00")) //EST
    val yest= currentDateTime.minus(Duration.ofHours(12))

    val archiveDateTime={
      val date1="2022_03_09_11_52_23".split("_").map(x =>x.toInt)
      LocalDateTime.of(date1.head, date1(1), date1(2), date1(3), date1(4),date1(5))
    }

    // If we received archiveDate in last 12 hrs
    archiveDateTime.isAfter(yest)

  }

  def main(args:Array[String]):Unit={

    subtractHoursFromCurrentTimestamp(12)

    val date1="2022_03_09_21_52_23".split("_").map(x =>x.toInt)
    val a = LocalDateTime.of(date1.head, date1(1), date1(2), date1(3), date1(4),date1(5))
    val b = LocalDateTime.now(ZoneId.of("-05:00")) //EST

    println(a.isEqual(b))
    println(a.compareTo(b))
    println(a == b)

    println(getDifferenceInHours("2022_03_09_21_52_23".split("_").map(x =>x.toInt), "2022_03_10_21_52_23".split("_").map(x =>x.toInt)))

  }

}
