Start the socket using nc -lk 9999
Use the below data to test the scnarios on spark documentation:
2019-01-29 12:02:00,cat dog
2019-01-29 12:03:00,dog dog
2019-01-29 12:04:00,cat doggy
2019-01-29 13:07:00,owl cat

2019-01-29 12:07:00,owl cat
2019-01-29 12:11:00,dog
2019-01-29 12:13:00,owl

--------------------------------------------------------------------------------------------------------
Start Spark-shell to consume the data:
import org.apache.spark.sql.streaming.Trigger

// Read text from socket
val lines = spark.readStream
  .format("socket")
  .option("host", "localhost")
  .option("port", 9999)
  .load()
  
val df2= lines.as[String].map(_.split(","))
val extractTimeDF= df2.withColumn("timestamp",col("value").getItem(0)).withColumn("word",col("value").getItem(1)).drop("value")

val wordArrayDF = extractTimeDF.withColumn("word",split(col("word")," "))
val words  = wordArrayDF.withColumn("word",explode(col("word")))

// Group the data by window and word and compute the count of each group
val windowedCounts = words.groupBy(
  window($"timestamp", "10 minutes", "3 minutes"),
  $"word"
).count()
  
val query = windowedCounts.writeStream
  .outputMode("complete")
  .option("truncate","false")
  .trigger(Trigger.ProcessingTime("1 minutes"))
  .format("console")
  .start()

query.awaitTermination()

