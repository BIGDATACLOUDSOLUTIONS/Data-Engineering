import org.apache.spark.sql.SparkSession
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.conf.Configuration

/**
 * Check if the table is partitioned:
 *  If Yes, iterate through each partitions, 
 *      If the corresponding hdfs location is empty/not Exists, drop the partitions and based on table type(Managed/External), delete the hdfs dir if it exists.
 *  
 *  After dropping all the partitions in above step, check the hdfs base location of table:
 *  If Base Location of Table is Empty/Not Exists, Drop the Table and based on table type(Managed/External), delete the hdfs dir if it exists.
 * 
 *  //Assumption:
 *   1. All Partitions of a partitioned Table are created under the same table's base location.
 *   2. Any table which doesn't have full read/write access will not be deleted.
 *   3. Any Views will not be deleted.
 */


val conf: Configuration = spark.sparkContext.hadoopConfiguration
val fs = FileSystem.get(conf)

//check if hdfs location is empty, non empty, does not exist
def hdfsDirectoryStatus(location: String): String = {
  val hdfsPath = new Path(location)
  if (fs.exists(hdfsPath)) {
    if (fs.listStatus(hdfsPath).length.equals(0)) "empty" else "nonEmpty"
  } else "notExists"
}

//Delete Empty HDFS Directory
def dropHDFSDir(location: String): Unit = {
  val hdfsPath = new Path(location)
  if (hdfsDirectoryStatus(location).equalsIgnoreCase("empty")) fs.delete(hdfsPath, true)
}


def dropEmptyPartitions(databaseName: String, tableName: String, tableBaseLocation: String, tableType: String): Unit = {
  val allPartitionsList = spark.sql(s"show partitions $databaseName.$tableName").collect.map(x => x(0).toString)

  //check corresponding location of partitions--> If empty or notExists drop the partitions --> delete hdfs dir
  allPartitionsList.foreach(partition => {
    val partitionUnderLyingHDFSLocation = s"$tableBaseLocation/$partition"

    if (!hdfsDirectoryStatus(partitionUnderLyingHDFSLocation).equalsIgnoreCase("nonEmpty")) {
      val partitionsString = partition.split("/").map(x => x.split("=")).map(x => s"${x(0)}='${x(1)}'").mkString(",")
      val dropPartitionsStatement = s"ALTER TABLE $databaseName.$tableName DROP IF EXISTS PARTITION ($partitionsString)"
      //spark.sql(dropPartitionsStatement)
      println(s"Dropped Partition: $partitionsString")

      if (tableType.equalsIgnoreCase("external")) {
        //dropHDFSDir(partitionUnderLyingHDFSLocation)
        println(s"Deleted empty Partition's HDFS Dir(if existed): $partitionUnderLyingHDFSLocation")
      }
    }
  })
}

val databaseName = "analyticscustomerprofile"
val listOfTableToExclude: List[String] = List()

val tableMetadataDataArray = spark.catalog.listTables(databaseName).select("name", "tableType").filter("tableType is not null and upper(tableType)<>'VIEW'").collect
val tableMetadataDataMap =tableMetadataDataArray.map(x => x(0).toString -> x(1).toString).toMap.filter(x => !listOfTableToExclude.contains(x._1))

tableMetadataDataMap.foreach(x => {
  val tableName = x._1
  val tableType = x._2

  println(s"Input Table Name: $tableName")
  println(s"Input Table's Type: $tableType")

  //Check if Table is Empty --> If tableHDFSLocation is empty or notExists, means table is empty
  val tableHDFSLocation = spark.sql(s"describe formatted $databaseName.$tableName").filter("col_name='Location'").select("data_type").collect.map(x => x(0).toString).head
  
  //Check if Table is partitioned, if Yes, drop the partitions and delete the hdfs directory
  val partitionsColumnsList = spark.catalog.listColumns(s"$databaseName.$tableName").filter("isPartition = 'true'").select("name").collect.map(x => x.mkString)
  if (partitionsColumnsList.nonEmpty) {
      //If table is partitioned, check corresponding location of partitions
      dropEmptyPartitions(databaseName, tableName, tableHDFSLocation, tableType)
    }

  if (!hdfsDirectoryStatus(tableHDFSLocation).equalsIgnoreCase("nonEmpty")) {
    if (tableType.equalsIgnoreCase("external")) {
      //spark.sql(s"drop table $databaseName.$tableName")
      //dropHDFSDir(tableHDFSLocation)
      println(s"Dropped Empty External Table: $databaseName.$tableName")
      println(s"Deleted the empty HDFS Dir(if existed): $tableHDFSLocation")
    } else {
      //spark.sql(s"drop table $databaseName.$tableName")
      println(s"Dropped Empty Managed Table: $databaseName.$tableName")
    }
    //If Table is not Empty
  }
  println("--------------------------------------")
})


//In case table partitions's hdfs location is pointing to location other than tables's base location, Use Below code to get partitionUnderLyingHDFSLocation dynamically. Use it only if All Partitions of the partitioned Table are NOT created under the same table's base location
/**
val partitionUnderLyingHDFSLocation:String={
val partitionsString=partition.split("/").map(x => x.split("=")).map(x => s"${x(0)}='${x(1)}'").mkString(",")
spark.sql(s"describe formatted $databaseName.$tableName partition($partitionsString)")
  .filter("col_name='Location'").select("data_type").collect.map(x => x(0).toString).filter(x =>x.contains(partition)).head
}
**/


Test the above code: rename table with some prefix to use like in select query
Database Name:practice


Case 1: UnderLying Location exists but is Empty
1.1: Managed Table:
use practice;
create table emp(a String); -->/apps/hive/warehouse/practice.db/emp

1.2: External Table:
use practice;
create external table emp_external(a string); --> /apps/hive/warehouse/practice.db/emp_external
 

Case 2: UnderLying Location doesnt exists
use practice;
create external table emp_external_2(a string); --> /apps/hive/warehouse/practice.db/emp_external_2
hdfs dfs -rm -r /apps/hive/warehouse/practice.db/emp_external_2

Case 3: UnderLying Location exists and it has data
3.1: Non Partitioned Table:
use practice;
create external table emp_external_3(a string); --> /apps/hive/warehouse/practice.db/emp_external_3
insert into table emp_external_3 values("rajesh");


3.2 Partitioned Table:
3.2.1 MAnaged:
use practice;
CREATE TABLE pageviews (userid STRING, link STRING, came_from STRING) PARTITIONED BY (datestamp STRING);

INSERT INTO TABLE pageviews PARTITION (datestamp = "2014-09-23") VALUES ("jsmith", "mail.com", "sports.com"), ("jdoe", "mail.com", null);
INSERT INTO TABLE pageviews PARTITION (datestamp = "2014-09-24") VALUES ("tjohnson", "sports.com", "finance.com"), ("tlee", "finance.com", "xyz.xom");
INSERT INTO TABLE pageviews PARTITION (datestamp = "2014-09-25") VALUES ("tjohnson", "sports.com", "finance.com"), ("tlee", "finance.com", "Bangalore");

Delete the data of one of the Partition
hdfs dfs -rm -r /apps/hive/warehouse/practice.db/pageviews/datestamp=2014-09-25


3.2.2 External:
use practice;
CREATE EXTERNAL TABLE pageviews_2 (userid STRING, link STRING, came_from STRING) PARTITIONED BY (datestamp STRING);

INSERT INTO TABLE pageviews_2 PARTITION (datestamp = "2014-09-23") VALUES ("jsmith", "mail.com", "sports.com"), ("jdoe", "mail.com", null);
INSERT INTO TABLE pageviews_2 PARTITION (datestamp = "2014-09-24") VALUES ("tjohnson", "sports.com", "finance.com"), ("tlee", "finance.com", "xyz.xom");
INSERT INTO TABLE pageviews_2 PARTITION (datestamp = "2014-09-25") VALUES ("tjohnson", "sports.com", "finance.com"), ("tlee", "finance.com", "Bangalore");


Delete the data of one of the Partition
hdfs dfs -rm -r /apps/hive/warehouse/practice.db/pageviews_2/datestamp=2014-09-25

///Run the spark code:
Expected:
emp --> Should Dropped
emp_external --> Should Dropped
emp_external_2 --> Should Dropped

emp_external_3 --> Should Not Drop

pageviews --> It should not have PARTITION (datestamp = "2014-09-25") in table and hdfs


pageviews_2 -->  It should not have PARTITION (datestamp = "2014-09-25") in table and hdfs



