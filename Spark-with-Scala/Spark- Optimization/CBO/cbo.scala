/**
Source:
YouTube: https://www.youtube.com/watch?v=Xam-a7GD9JM
YouTube Spark-Summit: https://www.youtube.com/watch?v=qS_aS99TjCM
Databricks: https://databricks.com/blog/2017/08/31/cost-based-optimizer-in-apache-spark-2-2.html

Start your spark shell on yarn because few feature won't work on local mode

spark2-shell --master yarn --deploy-mode client --driver-memory 2G
**/

spark.range(10000000).write.saveAsTable("rajeshkr.huge")
spark.range(10000000).withColumn("twice",col("id") *2).write.saveAsTable("rajeshkr.twice")
spark.range(1000).write.saveAsTable("rajeshkr.small")
spark.conf.get("spark.sql.cbo.enabled")

spark.table("rajeshkr.huge").join(spark.table("rajeshkr.twice"),"id").join(spark.table("rajeshkr.small"),"id").collect

Open the spark UI and Check the DAG in SQL Tab
Now we will enable CBO:
spark.conf.set("spark.sql.cbo.enabled","true")
spark.conf.set("spark.sql.cbo.joinReorder.enabled","true")

spark.sql("analyze table rajeshkr.huge compute statistics")
spark.sql("analyze table rajeshkr.twice compute statistics")
spark.sql("analyze table rajeshkr.small compute statistics")

Again Join the tables with CBO enabled:
spark.table("rajeshkr.huge").join(spark.table("rajeshkr.twice"),"id").join(spark.table("rajeshkr.small"),"id").collect

Now Check the efficiency of Join with filter:
1. With CBO disabled:
spark.conf.set("spark.sql.cbo.enabled","false")
spark.conf.set("spark.sql.cbo.joinReorder.enabled","false")

spark.table("rajeshkr.huge").join(spark.table("rajeshkr.twice").filter(col("twice") < 1500),"id").join(spark.table("rajeshkr.small"),"id").collect


2. With CBO enabled:
spark.conf.set("spark.sql.cbo.enabled","true")
spark.conf.set("spark.sql.cbo.joinReorder.enabled","true")

spark.sql("analyze table rajeshkr.huge compute statistics")
spark.sql("analyze table rajeshkr.twice compute statistics")
spark.sql("analyze table rajeshkr.small compute statistics")

spark.sql("analyze table rajeshkr.twice compute statistics for columns twice")
spark.table("rajeshkr.huge").join(spark.table("rajeshkr.twice").filter(col("twice") < 1500),"id").join(spark.table("rajeshkr.small"),"id").collect
