#!/usr/bin/env bash
source ${1}

spark-submit \
--master ${master} \
--deploy-mode ${deploy_mode} \
--num-executors ${num_executors} \
--executor-cores ${executor_cores} \
--driver-memory ${driver_memory} \
--executor-memory ${executor_memory} \
--class com.mytaxi.data.test.paymentservice.PaymentService \
--jars ${common_jar_path}/kafka-clients-2.1.0.jar,${common_jar_path}/config-1.3.2.jar,${common_jar_path}/scala-logging_2.12-3.9.0.jar /home/bigdatacloudsolution2501/applicationJars/paymentservice_2.11-0.1.0.jar ${topicName} ${bootStrapServer} ${client_id}
