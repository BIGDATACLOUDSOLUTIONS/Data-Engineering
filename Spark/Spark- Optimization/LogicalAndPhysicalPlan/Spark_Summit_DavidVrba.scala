/* 
Reference: Physical Plans in Spark SQL - David Vrba (Socialbakers)
Part1 -> https://www.youtube.com/watch?v=99fYi2mopbs
Part2 -> https://www.youtube.com/watch?v=9EIzhRKpiM8

PPT Availabe @: https://www.slideshare.net/databricks/physical-plans-in-spark-sql*/

// ---------------------------------------------Data Generation:--------------------------------------------------------------

import scala.util.Random
val random = new Random

def get_date:String={
  val yearRange = 2019 to 2020
  val year  =yearRange(random.nextInt(yearRange.length)).toString
  val monRange = 1 to 12
  val months= "%02d".format(monRange(random.nextInt(monRange.length))).toString
  val daysRange = 1 to 30
  val day= "%02d".format(daysRange(random.nextInt(daysRange.length))).toString
 
  s"""$year-$months-$day"""
}

val get_date_udf= udf(get_date _)

def getRandomInteractions:Int={
 val interactions = 10 to 99
 interactions(random.nextInt(interactions.length))
}

val getRandomInteractions_udf=udf(getRandomInteractions _)

def getRandomProfile_id:Int={
  val profile_ids = 1 to 100
      profile_ids(random.nextInt(profile_ids.length))
}

val getRandomProfileID_udf = udf(getRandomProfile_id _)

val df = spark.range(1000000).withColumnRenamed("id","post_id")
                             .withColumn("profile_id",getRandomProfileID_udf())
                             .withColumn("date",get_date_udf())
                             .withColumn("interactions",getRandomInteractions_udf())

df.cache

val posts=df.withColumn("date",to_date($"date")).withColumn("month",month($"date")) 

//Writing to Hive Table:
posts.write.format("orc").partitionBy("month").bucketBy(20,"profile_id").saveAsTable("rajeshkr.posts_fb")

//Reading from Hive Table:
//val posts=spark.table("rajeshkr.posts_fb").drop("month")


//Generate Profile Table:
def getRandomLang():String={
val langList=List("english","french","german","Japanese","Hindi")
  langList(random.nextInt(langList.length))
}
val getRandomLang_udf=udf (getRandomLang _)

def getRandomDesription():String={
  val descList=List("Something","Some Other Thing","Advanced Things","Some String")
  descList(random.nextInt(descList.length))
}

val getRandomDesription_udf=udf(getRandomDesription _)

val profiles=spark.range(100).withColumnRenamed("id","profile_id")
                             .withColumn("lang",getRandomLang_udf())
                             .withColumn("about",getRandomDesription_udf())


profiles.cache
/*---------------------------------------------------------------------------------------------------------------------------
                                                      EXAMPLE-1
---------------------------------------------------------------------------------------------------------------------------*/

//Take all profiles where sum of interaction is bigger than 100 or sum of interaction is less than 20
posts
.groupBy("profile_id")
.agg(sum("interactions").as("sum"))
.filter($"sum" >100 || $"sum"< 20)
.show() //-->Check Spark UI


// ---------------------------------------------------------------------------------------------------------------------------
//This we can do with by using union also like below:

val dfSumBig = posts
.groupBy("profile_id")
.agg(sum("interactions").as("sum"))
.filter($"sum" >100)


val dfSumSmall =posts
.groupBy("profile_id")
.agg(sum("interactions").as("sum"))
.filter($"sum"< 20)

dfSumBig.union(dfSumSmall).show() //-->Check Spark UI

/*
The sources of dfSumBig and dfSumSmall is same i.e df. If we do union, in this case there will only one source and one exchange
But if we add anything to any of the dataframe(as added below filter condtion in dfSumSmall, it will read the same data source twice)
In Spark2.4, there was no change 
-----------------------------------------------------------------------------------------------------------------------------
Let's suppose the assigment has changed:
For dfSumSmall we want to consider only specific profiles(profile_id is not null) */

val dfSumSmall =posts
.filter($"profile_id".isNotNull)
.groupBy("profile_id")
.agg(sum("interactions").as("sum"))
.filter($"sum"< 20)

dfSumBig.union(dfSumSmall).show() 

/* 
Check Spark UI In this case due to predicate push down, spark will push the filter near the source 
and hence it will create */

/*---------------------------------------------------------------------------------------------------------------------------
                                                      EXAMPLE-2
---------------------------------------------------------------------------------------------------------------------------*/
/*
Get only records (posts) with max interactions for each profile:
Assume profile_id has non-null values.
Three common ways how to write the query:
○ Using Window function
○ Using groupBy + join
○ Using correlated subquery in SQL

Which one is the most efficient?
*/

//Using Window:
import org.apache.spark.sql.expressions.Window

val w = Window.partitionBy("profile_id")

posts
.withColumn("maxCount", max("interactions").over(w))
.filter($"interactions" === $"maxCount")
.show()

//Using groupBy + join:
val maxCount = posts
.groupBy("profile_id")
.agg(max("interactions").alias("maxCount"))

posts.join(maxCount, Seq("profile_id"))
.filter($"interactions" === $"maxCount")
.show()

//Using correlated subquery (equivalent plan with Join+groupBy):
spark.conf.set("spark.sql.crossJoin.enabled", "true")
posts.createOrReplaceTempView("postsView")
val query = """
SELECT *
FROM postsView v1
WHERE interactions = (
select max(interactions) from postsView v2 where v1.profile_id = v2.profile_id
)
"""
spark.sql(query).show()
/* NOTE:
Window vs join + groupBy
If Both tables are large
● Go with window
● SortMergeJoin will be more expensive (3 exchanges, 2 sorts) */

/*---------------------------------------------------------------------------------------------------------------------------
                                                      EXAMPLE-3
---------------------------------------------------------------------------------------------------------------------------*/
// Sum interactions for each profile and each date
// Join additional table about profiles

posts
.groupBy("profile_id", "date")
.agg(sum("interactions"))
.join(profiles, Seq("profile_id"))


//optimized Way:
posts
.repartition(“profile_id”)
.groupBy(“profile_id”, “date”)
.agg(sum(“interactions”))
.join(profiles, Seq(“profile_id”))
