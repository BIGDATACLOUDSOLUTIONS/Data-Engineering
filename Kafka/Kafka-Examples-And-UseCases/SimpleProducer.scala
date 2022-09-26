import scala.util.Random
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object KafkaProducer {

  val props: Properties = new Properties()
  props.put("bootstrap.servers", "wn01.itversity.com:6667,wn02.itversity.com:6667,wn03.itversity.com:6667,wn04.itversity.com:6667")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)

  val key: String = "test_"
  val topic= "test_structuredstreming"

  def send(topic: String, payload: String): Unit = {
    val record = new ProducerRecord[String, String](topic, key, payload)
    producer.send(record)
  }

  def main(args: Array[String]): Unit = {

    for(ids <- 1 to 2) {
      val payload = generateMessage(ids)
      send(topic, payload)
      //Thread.sleep(1000)
    }
    producer.close()
  }

  def generateMessage(id:Int):String={

    val UserId= Random.alphanumeric.take(6).mkString("")
    val age= Random.nextInt(50)
    val salary= Random.nextFloat * 10000
    val message="{\"id\":"+"\""+id+"\","+"\"userId\":"+"\""+UserId+"\","+"\"age\":"+"\""+age+"\","+"\"salary\":"+"\""+salary+"\"}"
    message
  }

}
