Dynamic Partitioning: In overwrite mode: 

-------------------------------------------
CREATE EXTERNAL TABLE analyticscustomerprofile.emp_ine12370722(id string, first_name string, last_name string, email string, gender string) PARTITIONED BY (load_date string) ROW FORMAT SERDE 'org.apache.hadoop.hive.ql.io.parquet.serde.ParquetHiveSerDe'STORED AS INPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetInputFormat'OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.parquet.MapredParquetOutputFormat'LOCATION '/apps/hive/warehouse/analyticscustomerprofile.db/emp_ine12370722';

CREATE EXTERNAL TABLE analyticscustomerprofile.emp_ine12370722(id string, first_name string, last_name string, email string, gender string) PARTITIONED BY (load_date string) stored as orc LOCATION '/apps/hive/warehouse/analyticscustomerprofile.db/emp_ine12370722';

CREATE EXTERNAL TABLE analyticscustomerprofile.emp_ine12370722(id string, first_name string, last_name string, email string, gender string) PARTITIONED BY (load_date string) stored as parquet LOCATION '/apps/hive/warehouse/analyticscustomerprofile.db/emp_ine12370722';

Observation: If we create table from hive using stored as '', overwrite behaves differently

spark.conf.set("spark.sql.sources.partitionOverwriteMode","dynamic") // Default STATIC --> Important. 
This will allow spark to retain other partitions which are not there in dataframe to be written. This only overwrites the specific partitions.
-----------------------------------------------------------------------------------------------------------------------------------------------
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

val raw_df={spark
.read
.format("com.databricks.spark.csv")
.option("header","true")
.load("/user/maria_dev/dummy_data_2021.csv")
}

val partitionsColumnList= List("load_date")
val partitionsColumnStr=partitionsColumnList.mkString(",")

raw_df
.repartition(partitionsColumnList.map(c => col(c)): _*)
.write
.partitionBy(partitionsColumnStr)
.mode(SaveMode.Overwrite)
.saveAsTable("analyticscustomerprofile.emp_ine12370722")

spark.sql("show partitions analyticscustomerprofile.emp_ine12370722").show(false)
spark.sql("select load_date,count(*) from analyticscustomerprofile.emp_ine12370722 group by load_date").show(false)

-----------------------------------------------------------------------------------------------------------------------------------------------
Case 1: using partitionBy with saveAsTable. 
Note: No Need to enable any hive/spark properties for dynamic properties

raw_df
.filter("load_date ='2021-03-01'")
.repartition(partitionsColumnList.map(c => col(c)): _*)
.write
.partitionBy(partitionsColumnStr)
.mode(SaveMode.Overwrite)
.saveAsTable("analyticscustomerprofile.emp_ine12370722") 


hdfs dfs -ls /apps/hive/warehouse/analyticscustomerprofile.db/emp_ine12370722
//This will delete all other partitions and writes only load_date ='2021-03-01'

Checkout Difference between insertInto and saveAsTable

-----------------------------------------------------------------------------------------------------------------------------------------------

case 2: using partitionBy with saveAsTable. 

raw_df
.repartition(partitionsColumnList.map(c => col(c)): _*)
.write
.partitionBy(partitionsColumnStr)
.mode(SaveMode.Overwrite)
.insertInto("analyticscustomerprofile.emp_ine12370722")

ERROR: org.apache.spark.sql.AnalysisException: insertInto() can't be used together with partitionBy()
-----------------------------------------------------------------------------------------------------------------------------------------------

case 3: using only insertInto.

Note: No Need to enable any hive/spark properties for dynamic properties

raw_df
.repartition(partitionsColumnList.map(c => col(c)): _*)
.write
.mode(SaveMode.Overwrite)
.insertInto("analyticscustomerprofile.emp_ine12370722")

//This will also delete all other partitions and writes again

spark.sql("show partitions analyticscustomerprofile.emp_ine12370722").show(false)
spark.sql("select load_date,count(*) from analyticscustomerprofile.emp_ine12370722 group by load_date").show(false)
-----------------------------------------------------------------------------------------------------------------------------------------------
Case 4: Use insertInto with
spark.conf.set("spark.sql.sources.partitionOverwriteMode","dynamic")

raw_df
.filter("load_date ='2021-03-01'")
.repartition(partitionsColumnList.map(c => col(c)): _*)
.write
.mode(SaveMode.Overwrite)
.insertInto("analyticscustomerprofile.emp_ine12370722")

//Here only the data available in dataframe get updated, rest remain intact.
----------------------------------------------------------------------------------------------------------------------------------------------

case 4: hive query using spark-sql without partitions(load_date)

raw_df.write.mode(SaveMode.Overwrite).saveAsTable("analyticscustomerprofile.emp_ine12370722_tmp")

spark.sql("insert overwrite table analyticscustomerprofile.emp_ine12370722 select * from analyticscustomerprofile.emp_ine12370722_tmp where load_date ='2021-03-02'")

spark.sql("show partitions analyticscustomerprofile.emp_ine12370722").show(false)
spark.sql("select load_date,count(*) from analyticscustomerprofile.emp_ine12370722 group by load_date").show(false)
-----------------------------------------------------------------------------------------------------------------------------------------------
case 5: hive query using spark-sql with partitions(load_date)
spark.sql("insert overwrite table analyticscustomerprofile.emp_ine12370722 partition(load_date) select * from analyticscustomerprofile.emp_ine12370722_tmp")

spark.sql("show partitions analyticscustomerprofile.emp_ine12370722").show(false)
spark.sql("select load_date,count(*) from analyticscustomerprofile.emp_ine12370722 group by load_date").show(false)
-----------------------------------------------------------------------------------------------------------------------------------------------
IN CASE OF SPARK, IRRESPECTIVE OF DATA/PARTITIONS PRESENT IN THE TARGET TABLE, SPARK Overwrite COMPLETE TABLE. IT REFRESHES THE COMPLETE TABLE
AND RESET THE PARTITIONS.
-----------------------------------------------------------------------------------------------------------------------------------------------

case 6: Using Beeline: Load data from temp table to target table with overwrite:
Here I have to enable Hive dynamic partitions:
set hive.exec.dynamic.partition.mode=nonstrict;
set hive.exec.dynamic.partition=true;


beeline> insert overwrite table analyticscustomerprofile.emp_ine12370722 partition(load_date) select * from analyticscustomerprofile.emp_ine12370722_tmp where load_date ='2021-03-02';

show partitions analyticscustomerprofile.emp_ine12370722;
select load_date,count(*) from analyticscustomerprofile.emp_ine12370722 group by load_date;


In case of Hive/Beeline: Whatever partitions data is there in select statement of the temp table, only that partitions gets overwritten, other partitions remain untouched.

-----------------------------------------------------------------------------------------------------------------------------------------------



