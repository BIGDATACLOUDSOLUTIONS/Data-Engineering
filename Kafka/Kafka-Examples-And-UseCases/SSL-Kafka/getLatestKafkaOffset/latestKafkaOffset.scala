package getLatestKafkaOffset

import java.io.File
import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition

import scala.collection.JavaConversions._
import com.typesafe.config.{Config, ConfigFactory}

import scala.io.Source

object latestKafkaOffset {

  def getKafkaOffsetInfo(topicName:String,configFilePath:String):Unit = {
    var kafkaPartitionsList: List[Int] = List()
    var maxOffsetPerPartitionMap: Map[Int,Long] = Map()

    val props = new Properties()
    val conf :Config = ConfigFactory.parseFile(new File(configFilePath))
    props.put("bootstrap.servers", conf.getString("consumer.bootstrap_server"))
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    props.put("security.protocol", "SSL")
    props.put("ssl.truststore.location", conf.getString("consumer.ssl_truststore_location"))
    props.put("ssl.truststore.password", conf.getString("consumer.ssl_truststore_password"))
    props.put("ssl.keystore.location", conf.getString("consumer.ssl_keystore_location"))
    props.put("ssl.keystore.password", conf.getString("consumer.ssl_keystore_password"))
    props.put("ssl.key.password", conf.getString("consumer.ssl_key_password"))


    val kafkaConsumer: KafkaConsumer[String,String] = new KafkaConsumer[String,String](props)
    val topicPartitions: java.util.List[TopicPartition] = new util.ArrayList[TopicPartition]()

    val kafkaPartitions = kafkaConsumer.partitionsFor(topicName)

    val partitionIterator = kafkaPartitions.iterator()

    while(partitionIterator.hasNext){
      kafkaPartitionsList = kafkaPartitionsList ++ List(partitionIterator.next().partition())
    }

    kafkaPartitionsList.foreach{p =>
      topicPartitions.add(new TopicPartition(topicName,p))
    }

    kafkaConsumer.assign(topicPartitions)
    kafkaConsumer.poll(0)
    kafkaConsumer.seekToEnd(topicPartitions)
    val maxOffsetPerPartition =
      topicPartitions.map(x => x -> kafkaConsumer.position(x))



    maxOffsetPerPartition.foreach { x =>
      val key: Int = x._1.partition()
      val value: Long = x._2
      maxOffsetPerPartitionMap = maxOffsetPerPartitionMap ++ Map(key -> value)
    }


    val offsetString=maxOffsetPerPartitionMap.toList.map{ x=>
       "\"" + x._1 + "\":" + x._2
    }.mkString(",")

    val finalOffsetString="{\"" + topicName + "\":{" +  offsetString + "}}"
    println(finalOffsetString)
  }

  def main(args: Array[String]): Unit = {
    val topicList=Source.fromFile(args(0)).getLines().toList
    val appconfigFilePath=args(1)
    topicList.foreach { x =>
      getKafkaOffsetInfo(x, appconfigFilePath)
    }
  }

}
