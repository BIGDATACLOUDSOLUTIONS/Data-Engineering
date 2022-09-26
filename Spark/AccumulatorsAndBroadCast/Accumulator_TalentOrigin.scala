//Source: https://www.youtube.com/watch?v=BwrVCpTnDt8
//Data: https://raw.githubusercontent.com/TalentOrigin/datasets/master/sdg_datasets/flight-data/csv/2010-summary.csv

//Problem Statement: How many number of flights are there from/to from a particular country.
//Note that this can be done using simple spark sql API. But for purpose of demo, Accumulator is used.

//Code:


import org.apache.spark.sql.SparkSession
import org.apache.spark.util.LongAccumulator

object SparkAccumulatorsExample {
  def main(args: Array[String]): Unit = {
    val spark  = SparkSession
      .builder()
      .appName("Broadcast variable demo")
      .getOrCreate()


    val df = spark.read.option("header", "true")
      .option("inferSchema", "true")
      .csv("file:///home/maria_dev/datasets/unsorted/accumulator_talent_origin.csv")

    // Unnamed Accumulator
    val unitedStates = new LongAccumulator
    spark.sparkContext.register(unitedStates)

    // Named Accumulator
    val india = new LongAccumulator
    spark.sparkContext.register(india, "IndiaAcc")

    // 2nd Way of creating Accumulator
    val china = spark.sparkContext.longAccumulator("ChinaAcc")

    df.foreach {
      row =>
        val dest = row(0).toString
        val origin = row(1).toString
        val count = row(2).toString.toLong

        if(dest.equalsIgnoreCase("united states") || origin.equalsIgnoreCase("united states")) {
          unitedStates.add(count)
        }

        if(dest.equalsIgnoreCase("india") || origin.equalsIgnoreCase("india")) {
          india.add(count)
        }

        if(dest.equalsIgnoreCase("china") || origin.equalsIgnoreCase("china")) {
          china.add(count)
        }
    }

    println(s"--------------- United States: ${unitedStates.value}")
    println(s"--------------- India: $india")
    println(s"--------------- China: $china")

  }
}
