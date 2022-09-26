import java.text.SimpleDateFormat
import java.util.{Calendar, Date, Properties, TimeZone}
import java.sql.{Connection, DriverManager, Statement}



  
val now: Calendar = Calendar.getInstance()
val sdf = new SimpleDateFormat("yy-MM-dd");sdf.format(now.getTime) // res20: String = 20-02-27
val sdf = new SimpleDateFormat("yyyy-MM-dd");sdf.format(now.getTime)  //res21: String = 2020-02-27
  
  
val minute = now.get(Calendar.MINUTE)
val minute = now.get(Calendar.HOUR)
  
///////////////////////////////////////////////////////////////////
Add TimeZone:
  
import java.time.{ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS XXX")
val parsedDate = ZonedDateTime.parse("2019-11-08 05:28:01.002556 +01:00", formatter)
val targetFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS XXX")
targetFormat.format(parsedDate)

///////////////////////////////////////////////////////////////////
How to convert String to date time in Scala?

Method 1:
import java.time._
import java.time.format.DateTimeFormatter

val inputTime="2018-05-09 10:04:25.375"
val input_Date_format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
val required_Date_format = DateTimeFormatter.ofPattern("yyyyMMdd")


//String to Date objects 
val string_to_date = datetime_format.parse(inputTime)
java.time.temporal.TemporalAccessor = {},ISO resolved to 2018-05-08T21:01:15.402

//Date Back to string 
required_Date_format.format(string_to_date)


Method 2:

val inputTime="2018-05-09 10:04:25.375"
val format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
val string_to_date= format.parse(inputTime)
res43: java.util.Date = Wed May 09 10:04:25 IST 2018

val sdf = new SimpleDateFormat("yy-MM-dd");sdf.format(string_to_date) 
res44: String = 18-05-09


///////////////////////////////////////////////////////////////////

































