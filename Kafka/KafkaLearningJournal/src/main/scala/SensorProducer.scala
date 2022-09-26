import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

object SensorProducer {

  def main(args: Array[String]): Unit = {

    val props: Properties = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("partitioner.class", "Kafka.LearningJournal.SensorPartitioner")
    props.put("speed.sensor.name", "TSS")

    val topicName = "SensorTopic2"
    val producer = new KafkaProducer[String, String](props)

    for (i <- 1 to 10) {
      producer.send(new ProducerRecord[String, String](topicName, "SSP" + i, "500" + i))
    }
    for (i <- 1 to 10) {
      producer.send(new ProducerRecord[String, String](topicName, "TSS", "500" + i))
    }
    producer.close()
    println("SimpleProducer Completed.")

  }

}
