//Motivated From: https://blog.clairvoyantsoft.com/optimize-the-skew-in-spark-e523c6ee18ac?gi=c1503bfaaca7

import scala.util.Random
import scala.io._

//Data Generation
val random = new Random

def getRandomProductID: String = {
      val year = 101 to 999
      year(random.nextInt(year.length)).toString
 }

val getRandomProductID_Udf=udf(getRandomProductID _)

def getRandomCustID: String = {
      val custIDs = 1001 to 100000
      custIDs(random.nextInt(custIDs.length)).toString
 }

val getRandomCustID_UDF=udf(getRandomCustID _)


def getRandomCustName = {
    val r = new scala.util.Random
    val a = new Array[Char](5)
    val sb = new StringBuilder
    for (i <- 0 to 4) {
      a(i) = r.nextPrintableChar
    }
    a.mkString
}

val getRandomCustName_UDF= udf(getRandomCustName _)

/*
Tables:
order: order_id,product_id, cust_id
customer: cust_id,cust_name
*/

import spark.implicits._
val orders=spark.range(10e6.toLong).withColumnRenamed("id","order_id")
                              .withColumn("product_id",getRandomProductID_Udf())
                              .withColumn("cust_id",getRandomCustID_UDF())
                              .withColumn("cust_id", when($"cust_id" >4000, null) .otherwise($"cust_id"))
//orders.show
//orders.count
//orders.filter("cust_id is null").count
//orders.filter("cust_id is not null").count

val cust=spark.range(1000,100000).withColumnRenamed("id","cust_id").withColumn("cust_name",getRandomCustName_UDF())


/*
Requirement:
We have to do left join of orders and cust with orders on left.
The issue is that the data is skew for Null. The order table has more than 50% values for the cust_id as null.
We will join it like normal join and then we will do the same with small trick
*/

//Normal Join:
//Please check the spark UI for more details on join:


// Make sure that you don't end up with a BroadcastHashJoin and a BroadcastExchange
// For that, let's disable auto broadcasting
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", -1)

orders.join(cust, Seq("cust_id"),"left").collect 
/* This join might fail even. The key is distributed among multiple executor. 
All the null will go to single exector due to which all 199 task will completed very fast but the last task(handling null) might get stuck
due to skewness of data at null. To handle such scnarios, follow below steps */
 
/* Skewing Trick to Handle this Scenario:
Steps:
1. Divide the left table in 2 parts by using Filter: 
  1. Table containing all non null cust_id(non_null_order_df)
  2. Table containing all null cust_id(null_order_df)
  3. Join non_null_order_df with cust
  4. Add all the columns from right table except the joining key using withColumn and give the value as null
  5. Union the table from step 3 and step 4
  
*/
val non_null_order_df= orders.filter("cust_id is not null")
val non_null_joinedDF=  non_null_order_df.join(cust, Seq("cust_id"),"left")

val null_order_df = orders.filter("cust_id is null")
val nullDF = null_order_df.withColumn("cust_name",lit(null))

non_null_joinedDF.union(nullDF).collect //--> This will not fail in this scneario
  







