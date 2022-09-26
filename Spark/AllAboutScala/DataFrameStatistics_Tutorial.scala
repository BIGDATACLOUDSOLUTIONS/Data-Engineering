package spark.sql.batch.allaboutscala
import DataFrame_Tutorial.spark

object DataFrameStatistics_Tutorial extends App {

  val dfTags = spark
    .read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("file:///home/bigdatacloudsolution2501/datasets/spark/sparksql/question_tags_10K.csv")
    .toDF("id", "tag")

  val dfQuestionsCSV = spark
    .read
    .option("header", "true")
    .option("inferSchema", "true")
    .option("dateFormat","yyyy-MM-dd HH:mm:ss")
    .csv("file:///home/bigdatacloudsolution2501/datasets/spark/sparksql/questions_10K.csv")
    .toDF("id", "creation_date", "closed_date", "deletion_date", "score", "owner_userid", "answer_count")

  // cast columns to data types
  val dfQuestions = dfQuestionsCSV.select(
    dfQuestionsCSV.col("id").cast("integer"),
    dfQuestionsCSV.col("creation_date").cast("timestamp"),
    dfQuestionsCSV.col("closed_date").cast("timestamp"),
    dfQuestionsCSV.col("deletion_date").cast("date"),
    dfQuestionsCSV.col("score").cast("integer"),
    dfQuestionsCSV.col("owner_userid").cast("integer"),
    dfQuestionsCSV.col("answer_count").cast("integer")
  )

  // Average
  import org.apache.spark.sql.functions._
  dfQuestions
    .select(avg("score"))
    .show()


  // Max
  import org.apache.spark.sql.functions._
  dfQuestions
    .select(max("score"))
    .show()

  // Minimum
  import org.apache.spark.sql.functions._
  dfQuestions
    .select(min("score"))
    .show()

  // Sum
  import org.apache.spark.sql.functions._
  dfQuestions
    .select(sum("score"))
    .show()

  // Group by with statistics
  import org.apache.spark.sql.functions._
  dfQuestions
    .filter("id > 400 and id < 450")
    .filter("owner_userid is not null")
    .join(dfTags, dfQuestions.col("id").equalTo(dfTags("id")))
    .groupBy(dfQuestions.col("owner_userid"))
    .agg(avg("score"), max("answer_count"))
    .show()

  // DataFrame Statistics using describe() method
  val dfQuestionsStatistics = dfQuestions.describe()
  dfQuestionsStatistics.show()

  // find all rows where answer_count in (5, 10, 20)
  val dfQuestionsByAnswerCount = dfQuestions
    .filter("owner_userid > 0")
    .filter("answer_count in (5, 10, 20)")

  // count how many rows match answer_count in (5, 10, 20)
  dfQuestionsByAnswerCount
    .groupBy("answer_count")
    .count()
    .show()


}
