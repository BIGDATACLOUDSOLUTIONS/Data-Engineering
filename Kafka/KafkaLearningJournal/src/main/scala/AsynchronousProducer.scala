import java.util._

import org.apache.kafka.clients.producer._

object AsynchronousProducer {

  @throws(classOf[Exception])
  def main(args: Array[String]): Unit = {
    val  topicName = "test"
    val key = "Key1"
    val value = "Value-1"

    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093")
    props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val  producer = new KafkaProducer [String,String](props)
    val  record = new ProducerRecord[String,String](topicName,key,value)

    producer.send(record, new MyProducerCallback)
    System.out.println("AsynchronousProducer call completed")
    producer.close()
  }
}


class MyProducerCallback extends Callback {
  @Override
  def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
    if (e != null) System.out.println("AsynchronousProducer failed with an exception")
    else System.out.println("AsynchronousProducer call Success:")
  }
}
