package retail

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{sum, round}
import org.apache.log4j._

import org.apache.spark.sql.functions.col

object GetDailyProductRevenue {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf=ConfigFactory.load()
    val props=conf.getConfig(args(0))


    val inputBaseDir=props.getString("input.base.dir")
    val outputBaseDir=props.getString("output.base.dir")

    val spark=if(args(0).equalsIgnoreCase("dev")){
      SparkSession
        .builder()
        .master("local[*]")
        .appName("Get Daily Product Revenue")
        .getOrCreate()
    } else{
      SparkSession
        .builder()
        .appName("Get Daily Product Revenue")
        .getOrCreate()
    }
    import spark.implicits._

    val orders = spark.read.
      schema("order_id INT, order_date TIMESTAMP, order_customer_id INT, order_status STRING").
      csv(s"$inputBaseDir/orders")

    val orderItems = spark.read.
      schema(s"""order_item_id INT,
    order_item_order_id INT,
    order_item_product_id INT,
    order_item_quantity INT,
    order_item_subtotal FLOAT,
    order_item_product_price FLOAT""").
      csv(s"$inputBaseDir/order_items")


    orderItems.printSchema
    orderItems.show

    val products = spark.read.
      schema(s"""product_id INT,
    product_category_id INT,
    product_name STRING,
    product_description STRING,
    product_price FLOAT,
    product_image STRING""").
      csv(s"$inputBaseDir/products")

    products.printSchema
    products.show


    import org.apache.spark.sql.functions.{count, lit}

/*    val orderStatusCount = orders.
      groupBy("order_status").
      agg(count(lit(1)).alias("order_count"))*/

    val ordersCompleted = orders.
      filter("order_status IN ('COMPLETE', 'CLOSED')")

    val joinResults = ordersCompleted.
      join(orderItems, ordersCompleted("order_id") === orderItems("order_item_order_id")).
      join(products, products("product_id") === orderItems("order_item_product_id")).
      select("order_date", "product_name", "order_item_subtotal")

    joinResults.show(false)

    val dailyProductRevenue = joinResults.
      groupBy("order_date", "product_name").
      agg(round(sum("order_item_subtotal"), 2).alias("revenue"))
    dailyProductRevenue.show()
    val dailyProductRevenueSorted = dailyProductRevenue.
      orderBy($"order_date", col("revenue").desc)

    dailyProductRevenueSorted.show(false)

    spark.conf.set("spark.sql.shuffle.partitions", "2")

    dailyProductRevenueSorted.
      write.
      mode("overwrite").
      csv(s"$outputBaseDir/daily_product_revenue")
  }
}
