use rajeshkr;
--drop table if exists deliverysaver_raw;
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

--drop table if exists deliverysaver;
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