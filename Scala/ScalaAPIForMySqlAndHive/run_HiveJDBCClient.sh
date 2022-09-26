HADOOP_HOME=/usr/hdp/2.6.5.0-292/hadoop
HIVE_HOME=/usr/hdp/2.6.5.0-292/hive
CLASSPATH=/usr/hdp/2.6.5.0-292/hadoop/hadoop-common-2.7.3.2.6.5.0-292.jar
for i in ${HIVE_HOME}/lib/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done
 
EXTERNAL_JAR=/home/maria_dev/external_jars
for i in ls ${EXTERNAL_JAR}/*.jar ; do
    CLASSPATH=$CLASSPATH:$i
done

jar_path=/home/maria_dev/applications/SCALA_API_CONN_MYSQL_HIVE/scalaapiformysql_2.11-0.1.jar
CLASSPATH=$CLASSPATH:${jar_path}

scala -cp $CLASSPATH HiveJDBCClient
