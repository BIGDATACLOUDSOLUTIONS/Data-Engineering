import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.errors.SerializationException
import java.io.UnsupportedEncodingException
import java.util._
import java.nio.ByteBuffer


class SupplierSerializer extends Serializer[Supplier] {

  private val encoding: String = "UTF8"

  @Override
   def configure(configs: Map[String, _], isKey: Boolean): Unit = {}
  // nothing to configure
  // nothing to configure

  override def serialize(topic: String, data: Supplier): Array[Byte] = {
    var sizeOfName: Int = 0
    var sizeOfDate: Int = 0
    var serializedName: Array[Byte] = null
    var serializedDate: Array[Byte] = null
    if (data == null) null
    else {
      serializedName = data.getName.getBytes(encoding)
      sizeOfName = serializedName.length
      serializedDate = data.getStartDate.toString.getBytes(encoding)
      sizeOfDate = serializedDate.length
      val buf: ByteBuffer = ByteBuffer.allocate(4 + 4 + sizeOfName + 4 + sizeOfDate)

      buf.putInt(data.getID)
      buf.putInt(sizeOfName)
      buf.put(serializedName)
      buf.putInt(sizeOfDate)
      buf.put(serializedDate)
      buf.array()
    }
  }

  override def close(): Unit = {}
  // nothing to do
  // nothing to do

}
