import java.util._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.ConsumerRecord

//remove if not needed
import scala.collection.JavaConversions._

object SupplierConsumer {

  def main(args: Array[String]): Unit = {
    val topicName: String = "SupplierTopic"
    val groupName: String = "SupplierTopicGroup"
    val props: Properties = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093")
    props.put("group.id", groupName)
    props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "Kafka.LearningJournal.SupplierDeserializer")

    val consumer: KafkaConsumer[String, Supplier] = new KafkaConsumer[String, Supplier](props)

    consumer.subscribe(Collections.singletonList(topicName))

    while (true) {
      val records: ConsumerRecords[String, Supplier] = consumer.poll(100)
      for (record <- records) {
        println(
          "Supplier id= " + String.valueOf(record.value().getID) +
            " Supplier  Name = " +
            record.value().getName +
            " Supplier Start Date = " +
            record.value().getStartDate.toString)
      }
    }
  }

