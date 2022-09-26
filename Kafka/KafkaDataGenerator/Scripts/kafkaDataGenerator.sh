source ${1}

kafka_jar_path=`find "$app_jar_path"/paymentservice_2.11-0.1.0_working.jar`
echo Application Jar Path : $kafka_jar_path

spark_submit="
spark-submit \
--master ${master} \
--deploy-mode ${deploy_mode} \
--num-executors ${num_executors} \
--executor-cores ${executor_cores} \
--driver-memory ${driver_memory} \
--executor-memory ${executor_memory} \
--class com.mytaxi.data.test.paymentservice.PaymentService \
--conf spark.yarn.submit.waitAppCompletion=false \
--jars ${common_jar_path}/kafka-clients-2.1.0.jar,${common_jar_path}/config-1.3.2.jar,${common_jar_path}/scala-logging_2.12-3.9.0.jar /home/bigdatacloudsolution2501/applicationJars/paymentservice_2.11-0.1.0.jar ${topicName} ${bootStrapServer} ${client_id}"

echo $spark_submit

$spark_submit
