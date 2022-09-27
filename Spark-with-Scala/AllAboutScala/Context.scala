package spark.sql.batch.allaboutscala
//Bootstrap a SparkSession

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
//Bootstrap a SparkSession
trait Context {

  lazy val sparkConf = new SparkConf()
    .setAppName("Learn Spark")
    .setMaster("local[*]")
    .set("spark.cores.max", "2")

  lazy val spark = SparkSession
    .builder()
    .config(sparkConf)
    //.config("spark.sql.warehouse.dir", "C:\\Users\\RAJESH\\Google Drive\\Harish")
    .getOrCreate()
}