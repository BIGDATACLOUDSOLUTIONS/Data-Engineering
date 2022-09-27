Reference Video:
https://www.youtube.com/watch?v=aNY5grGbtTA&feature=youtu.be

Data:
file1.txt
1 2 3 4 5
6 7 a 8 9
10 11 12 b c


file2.txt
21 22 23 a b
24 25 26 27 28
29 30 c d e



Tasks:
1. Remove all non numeric characters and move the remaining numeric characters.
Expected Output:
(1,2,3,4,5)
(6,7,8,9,)
(10,11,12,,)

2. Union the 2 datasets in a single dataset such that the rows from 1st dataset will be on top and the rows from 2nd dataset will be down
3. Find average of adjacent columns:
    ex: avg(1st and 2nd column)=(1+2+6+7+...+30)/12
        avg(2nd and 3rd column)=(2+3+7+8+....+30)/11
        avg(2nd and 3rd column)=(3+4+....+27)/8


Solution:
val rdd1=sc.textFile("file:///home/maria_dev/usecases/spark/hackathon/file1.txt")
rdd1.collect.foreach(println)

val rdd2=sc.textFile("file:///home/maria_dev/usecases/spark/hackathon/file2.txt")
rdd2.collect.foreach(println)

def remove_non_numeric_characters(x:String)={
val str=x.split(" ").map(x => x.replaceAll("\\D", "")).filter(_!="")
val raw = str ++ Array.fill(5-str.size)("")
(raw(0),raw(1),raw(2),raw(3),raw(4))
}

val rdd3=rdd1.map(remove_non_numeric_characters)
rdd3.collect.foreach(println)

val rdd4=rdd2.map(remove_non_numeric_characters)
rdd4.collect.foreach(println)

case class xyz(a:String,b:String,c:String,d:String,e:String)
val df=rdd3.union(rdd4).map(x => xyz(x._1,x._2,x._3,x._4,x._5)).toDF
df.show

scala> df.show
+---+---+---+---+---+
|  a|  b|  c|  d|  e|
+---+---+---+---+---+
|  1|  2|  3|  4|  5|
|  6|  7|  8|  9|   |
| 10| 11| 12|   |   |
| 21| 22| 23|   |   |
| 24| 25| 26| 27| 28|
| 29| 30|   |   |   |
+---+---+---+---+---+



df.createOrReplaceTempView("temp")
val df2= spark.sql("select collect_list(a) as a,collect_list(b) as b,collect_list(c) as c,collect_list(d) as d,collect_list(e) as e from temp")

def getAvg(x:Seq[String],b:Seq[String]):Double={
val finalSeq= (x++b).filter(_!="").map(x =>x.toInt)
val avg=finalSeq.sum/finalSeq.size
avg
}

/*
//This is just check the sum and length
def getAvg(x:Seq[String],b:Seq[String]):String={
val finalSeq= (x++b).filter(_!="").map(x =>x.toInt)
s"Sum=${finalSeq.sum},Length=${finalSeq.length}"
}
*/

val getAvgUdf=udf(getAvg _)

val finalDF=df2
.withColumn("avg1",getAvgUdf(col("a"),col("b")))
.withColumn("avg2",getAvgUdf(col("b"),col("c")))
.withColumn("avg3",getAvgUdf(col("c"),col("d")))
.withColumn("avg4",getAvgUdf(col("d"),col("e")))
.select("avg1","avg2","avg3","avg4")
finalDF.show(false)




