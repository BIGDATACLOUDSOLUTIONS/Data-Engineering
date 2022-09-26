create table temp_emp(
id int,
first_name string,
last_name string,
email string,
gender string,
ip_address string)
row format delimited
fields terminated by ',';


set hive.support.concurrency = true;
set hive.enforce.bucketing = true;
set hive.exec.dynamic.partition.mode = nonstrict;
set hive.txn.manager = org.apache.hadoop.hive.ql.lockmgr.DbTxnManager;
set hive.compactor.initiator.on = true;
set hive.compactor.worker.threads = a positive number on at least one instance of the Thrift metastore service;

CREATE TABLE base_emp(
id int,
first_name string,
last_name string,
email string,
gender string,
ip_address string)
clustered by (id)
into 5 buckets
stored as orc TBLPROPERTIES('transactional'='true');


insert into base_emp select * from temp_emp;

select * from base_emp order by id ;

update base_emp set gender='Female' where id = 10;

delete from base_emp where id is null
