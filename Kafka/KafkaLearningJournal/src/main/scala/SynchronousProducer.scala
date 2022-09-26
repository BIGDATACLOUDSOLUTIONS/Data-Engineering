import java.util._
import org.apache.kafka.clients.producer._

object SynchronousProducer {

  def main(args: Array[String]): Unit = {
    val props: Properties = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)

    val key: String = "test_"
    val topic= "test"


    val record = new ProducerRecord[String, String](topic, key, "SynchronousProducerTopic")
    try{
      val metadata = producer.send(record).get
      println("Message is sent to Partition no " + metadata.partition + " and offset " + metadata.offset)
      println("SynchronousProducer Completed with success.")
    } catch {
      case e:Exception =>
      e.printStackTrace()
      System.out.println("SynchronousProducer failed with an exception")
    }finally{
      producer.close()
    }
  }

}
