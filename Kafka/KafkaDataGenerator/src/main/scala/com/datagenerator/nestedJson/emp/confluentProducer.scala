package com.datagenerator.nestedJson.emp

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.log4j.Logger

object confluentProducer  {
  val logger=Logger.getLogger(getClass.getName)

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, generateEmpData.bootStrapServerIP)
  props.put(ProducerConfig.CLIENT_ID_CONFIG, "emp_producer")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val key: String = "emp"

  def send(topic: String, payload: String): Unit = {
    val record = new ProducerRecord[String, String](topic, key, payload)
    producer.send(record)
    logger.info(record)
  }
}