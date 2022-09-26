import java.util._
import org.apache.kafka.clients.consumer._
import scala.collection.JavaConverters._
import java.util

object Consumer{

  def consumeFromKafka(topic: String) = {
    val props = new Properties()
    props.put("bootstrap.servers", "192.168.1.106:9092,192.168.1.106:9093,192.168.1.106:9094")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "earliest")
    props.put("enable.auto.commit", "false")
    props.put("auto.commit.interval.ms", "1000")
    props.put("group.id", "consumer-group")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer(props)

    consumer.subscribe(util.Arrays.asList(topic))
    while (true) {
      val record = consumer.poll(10)
      for (data <- record.asScala)  println(data.value())
    }
  }
}
