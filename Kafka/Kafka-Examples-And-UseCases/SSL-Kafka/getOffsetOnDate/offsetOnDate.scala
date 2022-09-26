package getOffsetOnDate

import org.apache.log4j._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import scala.io.Source

object offsetOnDate {
  val log:Logger=Logger.getLogger(getClass.getName)

  lazy val spark = SparkSession
    .builder
    .master("local[*]")
    .getOrCreate()
  import spark.implicits._

  def getOffsetString(df: DataFrame):Unit = {
    val offsetDf =
      df.groupBy(col("topic"), col("partition")).agg(min("offset").as("offset"))

    val listOfPartitionOffsetMap = offsetDf
      .sort(col("partition"))
      .map { x =>
        (x(0).toString, s""""${x(1)}":${x(2)}""")
      }
      .collect
      .toList

    val offsetFinalSTring = if(listOfPartitionOffsetMap.nonEmpty) {
      val offsetValue = listOfPartitionOffsetMap
        .groupBy(_._1)
        .map(x => x._2.reduce((x, y) => (x._1, x._2 + "," + y._2)))
        .toList
        .head
      "{\"" + offsetValue._1 + "\":{" + offsetValue._2 + "}}"
    } else ""
    log.info(offsetFinalSTring)
    println(offsetFinalSTring)
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val topicPath=args(0)
    val sourcePropertiesPath=args(1)

    val sourceProperties = Source.fromFile(sourcePropertiesPath).getLines
      .map(x => x.split("="))
      .map(e => e(0) -> e(1))
      .toMap

    val topicList=Source.fromFile(topicPath).getLines().toList.distinct
    val filterDate="2019-11-08 00:00:00.000"
    println("filterDate: "+ filterDate)
    topicList.foreach{ x =>
      val rawDF = spark.read
        .format("kafka")
        .options(sourceProperties)
        .option("subscribe", x)
        .option("startingOffsets", "earliest")//"""{"test":{"0":10}}""")
        //.option("endingOffsets", """{"test":{"0":10}}""")
        .load
        .selectExpr("CAST(key AS STRING)",
          "CAST(value AS STRING)",
          "topic",
          "offset",
          "partition")
        .filter(s"timestamp >= '$filterDate'")
      getOffsetString(rawDF)
    }
  }

}
