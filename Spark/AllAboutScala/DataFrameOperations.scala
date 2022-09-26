package spark.sql.batch.allaboutscala
import org.apache.spark.sql.Dataset

object DataFrameOperations extends App with Context {

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
    .option("dateFormat", "yyyy-MM-dd HH:mm:ss")
    .csv("file:///home/bigdatacloudsolution2501/datasets/spark/sparksql/questions_10K.csv")
    .toDF("id", "creation_date", "closed_date", "deletion_date", "score", "owner_userid", "answer_count")

  val dfQuestions = dfQuestionsCSV
    .filter("score > 400 and score < 410")
    .join(dfTags, "id")
    .select("owner_userid", "tag", "creation_date", "score")
    .toDF()

  dfQuestions.show(10)

  //Convert DataFrame row to Scala case class
  case class Tag(id: Int, tag: String)

  import spark.implicits._

  val dfTagsOfTag: Dataset[Tag] = dfTags.as[Tag]
  dfTagsOfTag
    .take(10)
    .foreach(t => println(s"id = ${t.id}, tag = ${t.tag}"))

  //DataFrame row to Scala case class using map()
  case class Question(owner_userid: Int, tag: String, creationDate: java.sql.Timestamp, score: Int)

  // create a function which will parse each element in the row
  def toQuestion(row: org.apache.spark.sql.Row): Question = {
    // to normalize our owner_userid data
    val IntOf: String => Option[Int] = _ match {
      case s if s == "NA" => None
      case s => Some(s.toInt)
    }

    import java.time._
    val DateOf: String => java.sql.Timestamp = _ match {
      case s => java.sql.Timestamp.valueOf(ZonedDateTime.parse(s).toLocalDateTime)
    }

    Question(
      owner_userid = IntOf(row.getString(0)).getOrElse(-1),
      tag = row.getString(1),
      creationDate = DateOf(row.getString(2)),
      score = row.getString(3).toInt
    )
  }

  // now let's convert each row into a Question case class
  import spark.implicits._

  val dfOfQuestion: Dataset[Question] = dfQuestions.map(row => toQuestion(row))
  dfOfQuestion
    .take(10)
    .foreach(q => println(s"owner userid = ${q.owner_userid}, tag = ${q.tag}, creation date = ${q.creationDate}, score = ${q.score}"))


  //Create DataFrame from collection
  val seqTags = Seq(
    1 -> "so_java",
    1 -> "so_jsp",
    2 -> "so_erlang",
    3 -> "so_scala",
    3 -> "so_akka"
  )

  import spark.implicits._

  val dfMoreTags = seqTags.toDF("id", "tag")
  dfMoreTags.show(10)

  //DataFrame Union
  val dfUnionOfTags = dfTags
    .union(dfMoreTags)
    .filter("id in (1,3)")
  dfUnionOfTags.show(10)


  //DataFrame Intersection
  val dfIntersectionTags = dfMoreTags
    .intersect(dfUnionOfTags)
    .show(10)

  //Append column to DataFrame using withColumn()
  import org.apache.spark.sql.functions._

  val dfSplitColumn = dfMoreTags
    .withColumn("tmp", split($"tag", "_"))
    .select(
      $"id",
      $"tag",
      $"tmp".getItem(0).as("so_prefix"),
      $"tmp".getItem(1).as("so_tag")
    ).drop("tmp")
  dfSplitColumn.show(10)


  //Spark Functions
  //Create DataFrame from Tuples
  val donut = Seq(("plain donut", 1.50), ("vanilla donut", 2.0), ("glazed donut", 2.50))
  val newdf = spark.createDataFrame(donut).toDF("Donut Name", "Price")

  newdf.show()

  val column_Names: Array[String] = newdf.columns
  columnNames.foreach(name => println(s"$name"))

  //DataFrame column names and types
  val (columnNames, columnDataTypes) = newdf.dtypes.unzip

  //Json into DataFrame using explode()
  val tagsDF = spark
    .read
    .option("multiLine", true)
    .option("inferSchema", true)
    .json("file:////home/bigdatacloudsolution2501/datasets/spark/sparksql/tags_sample.json")

  val tags_explded_DF = tagsDF.select(explode($"stackoverflow") as "stackoverflow_tags")

  val flattenedDF = tags_explded_DF.select(
    $"stackoverflow_tags.tag.id" as "id",
    $"stackoverflow_tags.tag.author" as "author",
    $"stackoverflow_tags.tag.name" as "tag_name",
    $"stackoverflow_tags.tag.frameworks.id" as "frameworks_id",
    $"stackoverflow_tags.tag.frameworks.name" as "frameworks_name"
  )
  flattenedDF.show()


  //Concatenate DataFrames using join()
  val donuts_new1 = Seq(("111", "plain donut", 1.50), ("222", "vanilla donut", 2.0), ("333", "glazed donut", 2.50))
  val dfDonuts = spark
    .createDataFrame(donuts_new1)
    .toDF("Id", "Donut Name", "Price")

  dfDonuts.show()

  val inventory = Seq(("111", 10), ("222", 20), ("333", 30))
  val dfInventory = spark
    .createDataFrame(inventory)
    .toDF("Id", "Inventory")
  dfInventory.show()

  val dfDonutsInventory = dfDonuts.join(dfInventory, Seq("Id"), "inner")
  dfDonutsInventory.show()

  //Search DataFrame column using array_contains()
  flattenedDF
    .select("*")
    .where(array_contains($"frameworks_name", "Play Framework"))
    .show()

  //Check DataFrame column exists
  val priceColumnExists = dfDonuts.columns.contains("Price")

  //Split DataFrame Array column
  val targets = Seq(("Plain Donut", Array(1.50, 2.0)), ("Vanilla Donut", Array(2.0, 2.50)), ("Strawberry Donut", Array(2.50, 3.50)))
  val targetsDf = spark
    .createDataFrame(targets)
    .toDF("Name", "Prices")

  targetsDf.show()
  targetsDf.printSchema()

  val df2_getarray_element = targetsDf
    .select(
      $"Name",
      $"Prices" (0).as("Low Price"),
      $"Prices" (1).as("High Price")
    )
  df2_getarray_element.show()

  //Rename DataFrame column
  val donutsSeq = Seq(("plain donut", 1.50), ("vanilla donut", 2.0), ("glazed donut", 2.50))
  val donutsdf = spark.createDataFrame(donutsSeq).toDF("Donut Name", "Price")
  donutsdf.show()

  val renamedDF = donutsdf.withColumnRenamed("Donut Name", "Name")
  renamedDF.show()

  //Create DataFrame constant column
  import org.apache.spark.sql.functions._

  val df2_constant = donutsdf
    .withColumn("Tasty", lit(true))
    .withColumn("Correlation", lit(1))
    .withColumn("Stock Min Max", typedLit(Seq(100, 500)))

  df2_constant.show()

  //DataFrame new column with User Defined Function (UDF)
  val stockMinMax: (String => Seq[Int]) = (donutName: String) => donutName match {
    case "plain donut" => Seq(100, 500)
    case "vanilla donut" => Seq(200, 400)
    case "glazed donut" => Seq(300, 600)
    case _ => Seq(150, 150)
  }

  val udfStockMinMax = udf(stockMinMax)
  val df2_udf = df2_constant.withColumn("Stock Min Max", udfStockMinMax($"Donut Name"))
  df2_udf.show()

  //DataFrame First Row
  val donuts2 = Seq(("plain donut", 1.50), ("vanilla donut", 2.0), ("glazed donut", 2.50))
  val df2 = spark
    .createDataFrame(donuts2)
    .toDF("Donut Name", "Price")

  val firstRow = df2.first()
  println(s"First row = $firstRow")

  val firstRowColumn1 = df2.first().get(0)
  println(s"First row column 1 = $firstRowColumn1")

  val firstRowColumnPrice = df2.first().getAs[Double]("Price")
  println(s"First row column Price = $firstRowColumnPrice")

  //Format DataFrame column
  val donuts3 = Seq(("plain donut", 1.50, "2018-04-17"), ("vanilla donut", 2.0, "2018-04-01"), ("glazed donut", 2.50, "2018-04-02"))
  val df3 = spark.createDataFrame(donuts3).toDF("Donut Name", "Price", "Purchase Date")

  import org.apache.spark.sql.functions._
  import spark.implicits._

  df3
    .withColumn("Price Formatted", format_number($"Price", 2))
    .withColumn("Name Formatted", format_string("awesome %s", $"Donut Name"))
    .withColumn("Name Uppercase", upper($"Donut Name"))
    .withColumn("Name Lowercase", lower($"Donut Name"))
    .withColumn("Date Formatted", date_format($"Purchase Date", "yyyyMMdd"))
    .withColumn("Day", dayofmonth($"Purchase Date"))
    .withColumn("Month", month($"Purchase Date"))
    .withColumn("Year", year($"Purchase Date"))
    .show()

  //DataFrame column hashing

  /*Hash column: This column creates a hash values for column Donut Names. As per the Spark 2.0 API documentation, the hash() function makes use of the Murmur3 hash.
    MD5 column: This column creates MD5 hash values for column Donut Names.
    SHA-1 column: This column creates SHA-1 hash values for column Donut Names.
    SHA-2 column: This column creates SHA-2 hash values for column Donut Names. For SHA-2, you will have to specify a second parameter for the number of bits. In the example below, I have chosen 256 as the number of bits.
*/
  val donuts4 = Seq(("plain donut", 1.50, "2018-04-17"), ("vanilla donut", 2.0, "2018-04-01"), ("glazed donut", 2.50, "2018-04-02"))
  val df4 = spark.createDataFrame(donuts4).toDF("Donut Name", "Price", "Purchase Date")

  import org.apache.spark.sql.functions._
  import spark.implicits._

  df4
    .withColumn("Hash", hash($"Donut Name")) // murmur3 hash as default.
    .withColumn("MD5", md5($"Donut Name"))
    .withColumn("SHA1", sha1($"Donut Name"))
    .withColumn("SHA2", sha2($"Donut Name", 256)) // 256 is the number of bits
    .show()


  //DataFrame String Functions
  val donuts5 = Seq(("plain donut", 1.50, "2018-04-17"), ("vanilla donut", 2.0, "2018-04-01"), ("glazed donut", 2.50, "2018-04-02"))
  val df5= spark
    .createDataFrame(donuts5)
    .toDF("Donut Name", "Price", "Purchase Date")

  import org.apache.spark.sql.functions._
  import spark.implicits._

  df5
    .withColumn("Contains plain", instr($"Donut Name", "donut"))
    .withColumn("Length", length($"Donut Name"))
    .withColumn("Trim", trim($"Donut Name"))
    .withColumn("LTrim", ltrim($"Donut Name"))
    .withColumn("RTrim", rtrim($"Donut Name"))
    .withColumn("Reverse", reverse($"Donut Name"))
    .withColumn("Substring", substring($"Donut Name", 0, 5))
    .withColumn("IsNull", isnull($"Donut Name"))
    .withColumn("Concat", concat_ws(" - ", $"Donut Name", $"Price"))
    .withColumn("InitCap", initcap($"Donut Name"))
    .show()

  //DataFrame drop null
  val donuts = Seq(("plain donut", 1.50), (null.asInstanceOf[String], 2.0), ("glazed donut", 2.50))
  val dfWithNull = spark
    .createDataFrame(donuts)
    .toDF("Donut Name", "Price")

  dfWithNull.show()

  val dfWithoutNull = dfWithNull.na.drop()
  dfWithoutNull.show()




}
