Sources: https://jaceklaskowski.gitbooks.io/mastering-spark-sql/spark-sql-bucketing.html

Important Points:
1. To avoid the exchanges (and so optimize the join query) is to use table bucketing that is applicable for all file-based data 
sources, e.g. Parquet, ORC, JSON, CSV, that are saved as a table using DataFrameWrite.saveAsTable or simply available in a catalog by 
SparkSession.table.

2. Note: Bucketing is not supported for DataFrameWriter.save, DataFrameWriter.insertInto and DataFrameWriter.jdbc methods. 
