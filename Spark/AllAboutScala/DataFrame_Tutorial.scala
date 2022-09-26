package spark.sql.batch.allaboutscala
import org.apache.spark.sql.functions._
object DataFrame_Tutorial extends App with Context {

  //Create a DataFrame from reading a CSV file
  val dfTags = spark
    .read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("file:///home/bigdatacloudsolution2501/datasets/spark/sparksql/question_tags_10K.csv")
    .toDF("id", "tag")

  dfTags.show(10)

  //Print DataFrame schema
  dfTags.printSchema()

  //DataFrame Query: select columns from a dataframe
  dfTags.select("id", "tag").show(10)

  //DataFrame Query: filter by column value of a dataframe
  dfTags.filter("tag == 'php'").show(10)

  //DataFrame Query: count rows of a dataframe
  println(s"Number of php tags = ${ dfTags.filter("tag == 'php'").count() }")

  //DataFrame Query: SQL like query
  dfTags.filter("tag like 's%'").show(10)

  //DataFrame Query: Multiple filter chaining
  dfTags.filter("tag like 's%'").filter("id == 25 or id == 108").show(10)

  //DataFrame Query: SQL IN clause
  dfTags.filter("id in (25, 108)").show(10)

  //DataFrame Query: SQL Group By
  println("Group by tag value")
  dfTags.groupBy("tag").count().show(10)

  dfTags.groupBy("tag").agg(count("tag") as "tag_count").show(10)

  //DataFrame Query: SQL Group By with filter
  dfTags.groupBy("tag").count().filter("count > 5").show(10)

  //DataFrame Query: SQL order by
  dfTags.groupBy("tag").count().filter("count > 5").orderBy("tag").show(10) //increasing order

  import spark.implicits._
  dfTags.groupBy("tag").count().filter("count > 5").sort($"count".desc).show(10)

  //DataFrame Query: Cast columns to specific data type
  val dfQuestionsCSV = spark
    .read
    .option("header", "true")
    .option("inferSchema", "true")
    .option("dateFormat","yyyy-MM-dd HH:mm:ss")
    .csv("file:///home/bigdatacloudsolution2501/datasets/spark/sparksql/questions_10K.csv")
    .toDF("id", "creation_date", "closed_date", "deletion_date", "score", "owner_userid", "answer_count")

  dfQuestionsCSV.printSchema()
  //Although we've passed in the inferSchema option, Spark did not fully match the data type for some of our columns.Column closed_date is of type string and so is column owner_userid and answer_count.
  val dfQuestions = dfQuestionsCSV.select(
    dfQuestionsCSV.col("id").cast("integer"),
    dfQuestionsCSV.col("creation_date").cast("timestamp"),  //Default TimeStamp Format :2013-06-03 04:00:25
    dfQuestionsCSV.col("closed_date").cast("timestamp"),
    dfQuestionsCSV.col("deletion_date").cast("date"),  	  //Default Date Format :2013-06-03
    dfQuestionsCSV.col("score").cast("integer"),
    dfQuestionsCSV.col("owner_userid").cast("integer"),
    dfQuestionsCSV.col("answer_count").cast("integer")
  )

  dfQuestions.printSchema()
  dfQuestions.show(10,false)

  //DataFrame Query: Operate on a filtered dataframe
  val dfQuestionsSubset = dfQuestions.filter("score > 400 and score < 410").toDF()
  dfQuestionsSubset.show()

  //DataFrame Query: Join
  dfQuestionsSubset.join(dfTags, "id").show(10) //Inner Join by default

  //DataFrame Query: Join and select columns
  dfQuestionsSubset.join(dfTags, "id").select("owner_userid", "tag", "creation_date", "score").show(10)

  //DataFrame Query: Join on explicit columns
  dfQuestionsSubset.join(dfTags, dfTags("id") === dfQuestionsSubset("id")).show(10) //Here id column will come twice, one from each dataFrame

  //DataFrame Query: Inner Join
  dfQuestionsSubset.join(dfTags, Seq("id"), "inner").show(10)

  //DataFrame Query: Left Outer Join
  dfQuestionsSubset.join(dfTags, Seq("id"), "left_outer").show(10)

  //DataFrame Query: Right Outer Join
  dfTags.join(dfQuestionsSubset, Seq("id"), "right_outer").show(10)

  //DataFrame Query: Distinct
  dfTags.select("tag").distinct().show(10)


}