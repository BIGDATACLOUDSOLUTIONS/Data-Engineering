Sources:
https://www.linkedin.com/pulse/avro-schema-evolution-simplified-richard-kashyap-deka
https://acadgild.com/blog/avro-hive
http://bigdatafindings.blogspot.com/2016/05/schema-evolution-with-avro.html
#############################################################################

Method 1:
Original Avro schema file: department_avro_schema.avsc

{
  "type" : "record",
  "name" : "department_avro_schema",
  "doc" : "Schema For department Table",
  "fields" : [
  {
         "name" : "department_id",
         "type" : "int",
         "columnName" : "department_id"
  },
  { 
         "name" : "department_name",
         "type" : "string",
         "columnName" : "department_name"
  }
  ],
  "tableName" : "department"
}
We need to put this schema file inside a HDFS directory before creating the hive table. 
In my case, I have put the file inside /user/maria_dev/avro/ HDFS directory.
hdfs dfs -mkdir /user/maria_dev/avro
hadoop fs -put /home/maria_dev/department_avro_schema.avsc /user/maria_dev/avro/
hadoop fs -ls /user/maria_dev/avro/


drop table if exists department;
create external table department stored as avro location '/user/maria_dev/avro/avrohivetabledata' 
TBLPROPERTIES ('avro.schema.url' = 'hdfs:///user/maria_dev/avro/department_avro_schema.avsc');

insert into department values(1,"Operation");
insert into department values(2,"HR");
insert into department values(3,"Developers");
insert into department values(4,"Testers");

select * from department;

We will try to add two new columns in the department table, one (department_manager_id) at the middle of the existing columns department_id 
and department_name and one (department_loc) at the end. For doing that we need to change the schema file first. 
Also, we need to specify default values for the newly added columns, otherwise the hive table will not know how to
represent the old records for newly added columns.
Hence, the updated Avro schema file for the department table will look like as given below-


{
  "type" : "record",
  "name" : "department_avro_schema",
  "doc" : "Schema For department Table",
  "fields" : [
  {
         "name" : "department_id",
         "type" : "int",
         "columnName" : "department_id"
  },
   {
         "name" : "department_manager_id",
         "type" : "string",
         "columnName" : "department_manager_id",
         "default":"-1"
  },
  { 
         "name" : "department_name",
         "type" : "string",
         "columnName" : "department_name"
  },
  { 
         "name" : "department_loc",
         "type" : "string",
         "columnName" : "department_loc",
         "default":"-1"
  }
  ],
  "tableName" : "department"
}


hadoop fs -rm /user/maria_dev/avro/department_avro_schema.avsc
hadoop fs -put /home/maria_dev/department_avro_schema.avsc /user/maria_dev/avro/


insert into department values(5,"Manager-101","Marketing","Pune");
insert into department values(6,"Manager-102","Finance","Bangalore");
select * from department;

#############################################################################
Mathod:2
https://acadgild.com/blog/avro-hive
