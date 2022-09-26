import java.util.Date

class Supplier() {
  private var  supplierId:Int=_
  private var supplierName:String=_
  private var supplierStartDate:Date=_

  def this(id:Int, name:String, dt:Date){
    this()
    this.supplierId = id
    this.supplierName = name
    this.supplierStartDate = dt
  }

  def getID():Int={
    return supplierId
  }

  def getName():String={
    return supplierName
  }

  def getStartDate():Date={
    return supplierStartDate
  }

}
