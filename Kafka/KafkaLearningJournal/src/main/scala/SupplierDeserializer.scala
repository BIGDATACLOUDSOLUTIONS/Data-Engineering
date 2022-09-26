import java.nio.ByteBuffer
import java.util.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import java.io.UnsupportedEncodingException
import java.util.Map

class SupplierDeserializer extends Deserializer[Supplier]{

  private val encoding = "UTF8"

  @Override
  def configure(map: Map[String, _], b: Boolean): Unit = {}


  //def configure(configs: Map[String,_], isKey: Boolean): Unit =  ???

  @Override
  def deserialize(topic: String, data: Array[Byte]): Supplier = {

    try
        if (data == null) {
          println("Null recieved at deserialize")
          null
        }
      else{
          val buf = ByteBuffer.wrap(data)
          val id = buf.getInt
          val sizeOfName = buf.getInt
          val nameBytes: Array[Byte] = Array.ofDim[Byte](sizeOfName)
          buf.get(nameBytes)
          val deserializedName: String = new String(nameBytes, encoding)
          val sizeOfDate: Int = buf.getInt
          val dateBytes: Array[Byte] = Array.ofDim[Byte](sizeOfDate)
          buf.get(dateBytes)
          val dateString: String = new String(dateBytes, encoding)
          val df: DateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
          new Supplier(id, deserializedName, df.parse(dateString))
        }

    catch {
      case e: Exception => throw new Exception("Error when deserializing byte[] to Supplier")
    }

  }

  @Override
  def close(): Unit = {}


}
