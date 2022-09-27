Databricks: https://kb.databricks.com/_static/notebooks/data/bucketing-example.html

spark.sql("use rajeshkr")
import org.apache.spark.sql.functions._
def base = spark.range(1, 1600000, 1, 16).select($"id" as "key", rand(12) as "value")

// Write non-bucketed table
spark.sql("drop table unbucketed")
base.write.format("parquet").saveAsTable("unbucketed")

// Write bucketed table
spark.sql("drop table bucketed")
base.write.format("parquet").bucketBy(16, "key").sortBy("value").saveAsTable("bucketed")

val t1 = spark.table("unbucketed")
val t2 = spark.table("bucketed")
val t3 = spark.table("bucketed")

// Unbucketed - bucketed join. Both sides need to be repartitioned.
t1.join(t2, Seq("key")).explain()
t1.join(t2, Seq("key")).collect

// Unbucketed - bucketed join. Unbucketed side is correctly repartitioned, and only one shuffle is needed.
t1.repartition(16, $"key").join(t2, Seq("key")).explain()
t1.repartition(16, $"key").join(t2, Seq("key")).collect

// Unbucketed - bucketed join. Unbucketed side is incorrectly repartitioned, and two shuffles are needed
t1.repartition($"key").join(t2, Seq("key")).explain()

// bucketed - bucketed join. Both sides have the same bucketing, and no shuffles are needed.
t3.join(t2, Seq("key")).collect
