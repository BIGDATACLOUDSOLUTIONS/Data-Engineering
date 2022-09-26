Computer (Compid, Make, Model, Myear)
Employee (Id, Ename, Doj, Salary, Bonus, Dept, Designation, Manager, Compid)
---------------------------------------------------------------------------------------------
salesman (sid, sname, location)
product (prodid, pdesc, price, category, discount)
sale (saleid, sid, sldate, amount)
saledetail (saleid, prodid, quantity)
---------------------------------------------------------------------------------------------
Dept (Deptno, Dname, Loc)
Emp (Empno, Ename, Job, Mgr, Hiredate, Sal, Comm, Deptno)
Vehicle (Vehicleid, Vehiclename)
Empvehicle (Empno, Vehicleid)
---------------------------------------------------------------------------------------------
Item (Itemcode, Itemtype, Descr, Price, Reorderlevel, Qtyonhand, Category)
Quotation (Quotationid, Sname, Itemcode, Quotedprice, Qdate, Qstatus)
Orders (Orderid, Quotationid, Qtyordered, Orderdate, Status, Pymtdate, Delivereddate, Amountpaid, Pymtmode)
Retailoutlet (Roid, Location, Managerid)
Empdetails (Empid, Empname, Designation, Emailid, Contactno, Worksin, Salary)
Retailstock (Roid, Itemcode, Unitprice, Qtyavailable)