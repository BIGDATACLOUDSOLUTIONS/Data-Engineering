//Example 1:
val data = Seq(
       ("Ingestion", "Jerry", 1000), ("Ingestion", "Arya", 2000), ("Ingestion", "Emily", 3000),
       ("ML", "Riley", 9000), ("ML", "Patrick", 1000), ("ML", "Mickey", 8000),
       ("Analytics", "Donald", 1000), ("Ingestion", "John", 1000), ("Analytics", "Emily", 8000),
       ("Analytics", "Arya", 10000), ("BI", "Mickey", 12000), ("BI", "Martin", 5000))
 import spark.sqlContext.implicits._
 val df = data.toDF("Project", "Name", "Cost_To_Project")
 --pivot
 val pivotDF = df.groupBy("Name").pivot("Project").sum("Cost_To_Project")
 pivotDF.show()
 --unpivot
 val unPivotDF = pivotDF.select($"Name", expr("stack(4, 'Analytics', Analytics, 'BI', BI, 'Ingestion', Ingestion, 'ML', ML) as (Project, Cost_To_Project)")).where("Cost_To_Project is not null")
 unPivotDF.show() 
 
 
 //Example 2:

val data = Seq(("Banana",1000,"USA"), ("Carrots",1500,"USA"), ("Beans",1600,"USA"),
      ("Orange",2000,"USA"),("Orange",2000,"USA"),("Banana",400,"China"),
      ("Carrots",1200,"China"),("Beans",1500,"China"),("Orange",4000,"China"),
      ("Banana",2000,"Canada"),("Carrots",2000,"Canada"),("Beans",2000,"Mexico"))

import spark.sqlContext.implicits._
val df = data.toDF("Product","Amount","Country")
df.show()


Pivot Spark DataFrame:
val pivotDF = df.groupBy("Product").pivot("Country").sum("Amount")
pivotDF.show()

val pivotDF = df.groupBy("Product","Country")
      .sum("Amount")
      .groupBy("Product")
      .pivot("Country")
      .sum("sum(Amount)")
pivotDF.show()


Unpivot Spark DataFrame:

val unPivotDF = pivotDF.select($"Product",
expr("stack(3, 'Canada', Canada, 'China', China, 'Mexico', Mexico) as (Country,Total)"))
.where("Total is not null")
unPivotDF.show()


