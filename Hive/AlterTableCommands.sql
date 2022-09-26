-----------------------------ALTER COMMAND USAGE IN HIVE------------------------------------
--DEMO TABLE
create table citizen_par(first_name string,last_name string,company_name string,address string,city string,zip int,phone_1 string,phone_2 string ,email string,web string)
partitioned by (country string ,state string)
row format delimited
fields terminated by ','
stored as textfile;
------------------------------------------------------------------------
1. RENAME THE TABLE

SYNTAX: ALTER TABLE table_name RENAME TO new_table_name;

ALTER TABLE citizen_par RENAME TO citizen_partition;

DESCRIBE citizen_par;
------------------------------------------------------------------------
2. ALTER TABLE PROPERTIES

SYNTAX: ALTER TABLE table_name SET TBLPROPERTIES table_properties;
 
table_properties:
  : (property_name = property_value, property_name = property_value, ... )
  
DESCRIBE EXTENDED citizen_partition; --> CHECK THE COMMENT PART IT WILL BE SHOWING NULL
  
ALTER TABLE citizen_partition SET TBLPROPERTIES ('comment' = 'i have done alteration');

DESCRIBE EXTENDED citizen_partition; -->CHECK COMMENT PART AGAIN
-----------------------------------------------------------------------------
3.ADD PARTITION 

SYNTAX: ALTER TABLE table_name ADD [IF NOT EXISTS] PARTITION partition_spec [LOCATION 'location'][, PARTITION partition_spec [LOCATION 'location'], ...];
 
partition_spec:
  : (partition_column = partition_col_value, partition_column = partition_col_value, ...)
  
You can use ALTER TABLE ADD PARTITION to add partitions to a table. Partition values should be quoted only if they are strings. 
The location must be a directory inside of which data files reside. (ADD PARTITION changes the table metadata, but does not load data.
If the data does not exist in the partition's location, queries will not return any results.)
An error is thrown if the partition_spec for the table already exists. You can use IF NOT EXISTS to skip the error.

  
ALTER TABLE citizen_partition ADD PARTITION (country='demo1', state='demo2');
						  
describe formatted citizen_partition  PARTITION (country='demo1', state='demo2');

ALTER TABLE citizen_partition ADD PARTITION (country='demo3', state='demo4') PARTITION (country='demo5', state='demo6');

describe formatted citizen_partition PARTITION (country='demo3', state='demo4') ;
describe formatted citizen_partition PARTITION (country='demo5', state='demo6');


-FOR DYNAMIC PARTITIONING TO ADD PARTITIONING

For dynamic partitioning:

--Set following two properties for your Hive session:

SET hive.exec.dynamic.partition=true; SET hive.exec.dynamic.partition.mode=nonstrict;

--Create your staging table without Partition

create external table table_name (col_01,col_02) row format delimited fields terminated by ",";

--load data to your Hive Table:

load data local inpath '/path' into table table_name;

--Create production table with the columns you want to partition upon. In this case you want to partition upon date column:

create external table table_name (col_01,col_02) partitioned by (date_part string) row format delimited fields terminated by ',';

insert overwrite table table_name partition(date_part) select col_01,col_02 from staging_table;

--Verify data is correctly populated in partition:
----------------------------------------------------------------------------------
4.RENAME PARTITION

SYNTAX: ALTER TABLE table_name PARTITION partition_spec RENAME TO PARTITION partition_spec;

DESCRIBE citizen_partition;

ALTER TABLE citizen_partition PARTITION (country='demo5', state='demo6') RENAME TO PARTITION (country='demo7', state='demo8');

describe formatted citizen_partition PARTITION (country='demo5', state='demo6');

describe formatted citizen_partition PARTITION (country='demo7', state='demo8');

--for older version of hive, it may show error while renaming the partition , for that we have to set:
set fs.hdfs.impl.disable.cache=false; 
set fs.file.impl.disable.cache=false; 

-------------------------------------------------------------------------------------
5. DROP PARTITION

SYNTAX: ALTER TABLE <table_name> DROP PARTITION (<partition_col_name>='<value>');

ALTER TABLE citizen_partition DROP PARTITION (country='demo7', state='demo8');

describe formatted citizen_partition PARTITION (country='demo7', state='demo8');
---------------------------------------------------------------------------------------
6.ADD COLUMN

ADD COLUMNS lets you add new columns to the end of the existing columns but before the partition columns.

SYNTAX: ALTER TABLE table_name ADD COLUMNS ( column_name column_data_type COMMENT 'column name');

ALTER TABLE citizen_partition ADD COLUMNS ( hobby string COMMENT 'citizen hobby');

DESCRIBE citizen_partition;

---------------------------------------------------------------------------------------
7.REMOVE COLUMN
You cannot drop column directly from a table using command “ALTER TABLE table_name drop col_name;”
You can replace the column.REPLACE COLUMNS removes all existing columns and adds the new set of columns.

SYNTAX: ALTER TABLE table_name REPLACE COLUMNS ( existing_column_name existing_column_name_data_type new_column_name new_column_name_data_type);

ALTER TABLE citizen_partition REPLACE COLUMNS(address string);// this command will remove all the column except address, whatever be written in () should be there in table ,
 other than that all the other columns should be deleted.
 
DESCRIBE citizen_partition;

ALTER TABLE citizen_partition ADD COLUMNS ( hobby string COMMENT 'citizen hobby');

ALTER TABLE citizen_partition REPLACE COLUMNS address string hobby string;//not working
---------------------------------------------------------------------------------------
8.RENAME COLUMN

SYNTAX: ALTER TABLE table_name CHANGE (existing_column_name new_column_name new_column_name_data_type);

ALTER TABLE citizen_partition CHANGE address citizen_address  string;
--------------------------------------------------------------------------------------
9.CHANGE THE LOCATION OF TABLE

SYNTAX: ALTER TABLE table_name [PARTITION partition_spec] SET LOCATION "new location";

ALTER TABLE citizen_partition [PARTITION country PARTITION state] SET LOCATION "hdfs://quickstart.cloudera:8020/user/hive/warehouse/homework_1.db/emp_det";// not working
