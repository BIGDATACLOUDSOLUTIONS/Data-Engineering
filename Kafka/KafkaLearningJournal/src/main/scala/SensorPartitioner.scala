import java.util._
import org.apache.kafka.clients.producer._
import org.apache.kafka.common._
import org.apache.kafka.common.utils._
import org.apache.kafka.common.record._


class SensorPartitioner extends Partitioner {
  private var speedSensorName: String = _

  @Override
  def configure(configs: Map[String, _]): Unit = {
    speedSensorName = configs.get("speed.sensor.name").toString
  }

  @Override
  def partition(topic: String,
                key: AnyRef,
                keyBytes: Array[Byte],
                value: AnyRef,
                valueBytes: Array[Byte],
                cluster: Cluster): Int = {
    val partitions: List[PartitionInfo] = cluster.partitionsForTopic(topic)

    val numPartitions: Int = partitions.size
    val sp: Int = (numPartitions * 0.3).abs.toInt
    var p: Int = 0
    //println(s"numPartitions: $numPartitions")

    if ((keyBytes == null) || (!(key.isInstanceOf[String])))
      throw new InvalidRecordException(
        "All messages must have sensor name as key")

    p =
      if (key.asInstanceOf[String] == speedSensorName)
        Utils.toPositive(Utils.murmur2(valueBytes)) % sp
      else
        Utils.toPositive(Utils.murmur2(keyBytes)) % (numPartitions - sp) + sp
    println("Key = " + key.asInstanceOf[String] + " Partition = " + p)
    p
  }

  @Override
  def close(): Unit = {}


}
