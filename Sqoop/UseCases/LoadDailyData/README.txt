Import Customer Data to Hive from Mysql.
Job Run: Daily
Assume that the Sqoop job will run after the data is loaded at mysql server side.

Table Structure:
MySQL:
create table customer (
	empid INT,
	name VARCHAR(50),
	gender VARCHAR(50),
	salary INT,
	deptno VARCHAR(3),
	dataloaddate DATE
)

Hive Staging Table:
create external table if not exists rajeshkr.customer_raw(
empid INT,
name STRING,
gender STRING,
salary INT,
deptno STRING,
data_load_date STRING,
data_dwnld_date STRING
)
stored as textfile
location '/user/maria_dev/hive/tmp/customer_raw';

Hive Base Table:
create external table if not exists rajeshkr.customer(
empid INT,
name STRING,
gender STRING,
salary INT,
deptno STRING,
data_load_date STRING
)
partitioned by (data_dwnld_date STRING)
stored as parquet
location '/user/maria_dev/hive/data/customer';


--Steps:
1. Create the required Tables in Hive
2. Fetch the maximum value of data_load_date from hive 'customer' table.
3. Sqoop the data from mysql using sqoop on HDFS. While importing data, add one static column with system date and use the same 
for the data_dwnld_date. Import the data based on data_load_date. data_load_date should be greater than the value extracted in step2.
4. Create the staging table on top of the data imported in step 3.
5. Insert the data in partitioned table based on data_dwnld_date from raw table.

