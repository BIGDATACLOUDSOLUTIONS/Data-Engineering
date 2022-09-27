Difference Between saveAsTable and insertInto



insertInto --> Inserts the content of the DataFrame to the specified table. 
It requires that the schema of the DataFrame is the same as the schema of the table. 
Unlike saveAsTable, insertInto ignores the column names and just uses position-based resolution.


saveAsTable --> Saves the content of the DataFrame as the specified table.
In the case the table already exists, behavior of this function depends on the save mode, specified by the mode function (default to throwing an exception).
When mode is Overwrite, the schema of the DataFrame does not need to be the same as that of the existing table.
When mode is Append, if there is an existing table, we will use the format and options of the existing table. 
The column order in the schema of the DataFrame doesn't need to be same as that of the existing table. 
Unlike insertInto, saveAsTable will use the column names to find the correct column positions.



==> df.write.mode("overwrite").saveAsTable("schema.table") drops the existing table "schema.table" and recreates a new table based on the 'df' schema. The schema of the existing table becomes irrelevant and does not have to match with df. If your existing table was ORC and then the new table will be ecreated as parquet (Spark Default).

==> df.write.mode("overwrite").insertInto("schema.table") does not drop the existing table and expects the schema of the existing table to match with the schema of 'df'.

