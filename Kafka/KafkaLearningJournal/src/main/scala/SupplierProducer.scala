import java.util._
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.apache.kafka.clients.producer._
//remove if not needed
import scala.collection.JavaConversions._

object SupplierProducer {

  def main(args: Array[String]): Unit = {
    val topicName: String = "SupplierTopic"
    val props: Properties = new Properties()
    props.put("bootstrap.servers", "localhost:9092,localhost:9093")
    props.put("key.serializer",
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "Kafka.LearningJournal.SupplierSerializer")
    val producer: Producer[String, Supplier] =
      new KafkaProducer[String, Supplier](props)
    val df: DateFormat = new SimpleDateFormat("yyyy-MM-dd")

    val sp1: Supplier = new Supplier(101, "Xyz Pvt Ltd.", df.parse("2016-04-01"))
    val sp2: Supplier = new Supplier(102, "Abc Pvt Ltd.", df.parse("2012-01-01"))
    producer
      .send(new ProducerRecord[String, Supplier](topicName, "SUP", sp1))
      .get
    producer
      .send(new ProducerRecord[String, Supplier](topicName, "SUP", sp2))
      .get
    println("SupplierProducer Completed.")
    producer.close()
  }

}
