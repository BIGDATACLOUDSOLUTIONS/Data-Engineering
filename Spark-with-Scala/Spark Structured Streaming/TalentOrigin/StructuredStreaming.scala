package streaming

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types._
import org.apache.log4j._

object StructuredStreaming {
  def main(args: Array[String]): Unit = {
    val spark  = SparkSession.builder().appName("Spark Structured Streaming").master("local[*]").getOrCreate()
    //spark.sparkContext.setLogLevel("ERROR")
    Logger.getLogger("org").setLevel(Level.ERROR)

    val retailDataSchema = new StructType()
      .add("SerialNumber", IntegerType)
      .add("InvoiceNo", IntegerType)
      .add("StockCode", StringType)
      .add("Description", StringType)
      .add("Quantity", IntegerType)
      .add("InvoiceDate", StringType)
      .add("UnitPrice", DoubleType)
      .add("CustomerId", LongType)
      .add("Country", StringType)
      .add("InvoiceTimestamp", StringType)

    val streamingData = spark
      .readStream
      .schema(retailDataSchema)
      .csv("C:/Users/RAJESH/Desktop/Structured-Streaming/TalentOrigin/temp_working").drop("SerialNumber")


    val filteredData = streamingData//.filter("Country = 'United Kingdom'")

    val query = filteredData.writeStream
      .format("console")
      .queryName("filteredByCountry")
      .option("truncate","false")
      .outputMode(OutputMode.Append())
      .start()

    query.awaitTermination()
  }
}
