hive_beeline_url="'jdbc:hive2://localhost:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2' --silent=true"


beeline -u ${hive_beeline_url} -f /home/maria_dev/usecases/sqoop/deliverysaver/hivescript.sql

max_dataloaddate=`beeline -u ${hive_beeline_url} -e "select max(data_load_date) as processing_date from rajeshkr.customer" | grep -oP '[\d]+-[\d]+-[\d]+'`
where_cond="and dataloaddate>'$max_dataloaddate'"

if [[ $max_dataloaddate == '' ]];then
	  where_cond=""
fi


sqoop import \
--connect jdbc:mysql://localhost:3306/rajeshkr \
--username root \
--password hortonworks1 \
--query  "select empid,name,gender,salary,deptno,dataloaddate,CURDATE() from customer where \$CONDITIONS $where_cond" \
--target-dir /user/maria_dev/hive/tmp/customer_raw \
--split-by  empid \
--delete-target-dir \
--null-string '\\N' \
--null-non-string '\\N' \
--as-textfile \
--hive-delims-replacement ' ' \
--fields-terminated-by '\001'

beeline -u ${hive_beeline_url} -e "set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;
insert overwrite table rajeshkr.customer partition(data_dwnld_date) select * from rajeshkr.customer_raw;"
