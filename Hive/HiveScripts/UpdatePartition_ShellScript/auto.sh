FILE=/home/cloudera/Desktop/UpdateDynamicPartitions/job.properties
db_name=$(grep -i 'db_name' $FILE  | cut -f2 -d'=')
base_table=$(grep -i 'base_table' $FILE  | cut -f2 -d'=')
staging_table=$(grep -i 'staging_table' $FILE  | cut -f2 -d'=')
partition_col=$(grep -i 'partition_col' $FILE  | cut -f2 -d'=')
echo "Database Name: " $db_name
echo  "Base Table Name: " $base_table
echo  "Staging Table Name: " $staging_table
echo  "Partition Column Name: " $partition_col


var= $(hive -hiveconf DB_NAME=$db_name -hiveconf STAGING_TAB=$staging_table -hiveconf PARTITION_COL=$partition_col -S -e 'select distinct ${hiveconf:PARTITION_COL} from ${hiveconf:DB_NAME}.${hiveconf:STAGING_TAB};' 1>abcdef.txt)

sed -i.bak "/WARN/d" abcdef.txt

while read partition_val;
do
echo "$partition_val"
hive -hiveconf DB_NAME=$db_name -hiveconf BASE_TABLE=$base_table -hiveconf STAGING_TAB=$staging_table -hiveconf PARTITION_COL=$partition_col -hiveconf PARTITION_VALUE=$partition_val -S -f /home/cloudera/Desktop/UpdateDynamicPartitions/hive.hql
echo "Done_$partition_val"
done <abcdef.txt
