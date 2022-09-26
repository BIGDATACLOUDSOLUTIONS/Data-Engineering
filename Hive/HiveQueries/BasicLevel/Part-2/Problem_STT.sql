Computer (Compid, Make, Model, Myear)
Employee (Id, Ename, Doj, Salary, Bonus, Dept, Designation, Manager, Compid)


JOINS:
1. Display employee id, name and department along with computer id and make of computers allocated to them.
	select id,ename,dept,e.Compid,c.make from Employee e inner join computer c on e.Compid=c.Compid
2. Display id, name, computer id and make of the computers allocated to employees provided model is either 'Precision' or 'Edge'.
	select ID,ENAME,e.COMPID,c.MAKE from Employee e inner join Computer c on e.Compid=c.Compid and c.Model in ('Precision','Edge')

3. Display department and number of computers made by Dell allocated in that department.
	select e.Dept,count(c.Compid) as "NUMBER OF COMPUTERS" from Employee e inner join Computer c on e.Compid=c.Compid and c.MAKE in ('Dell') group by e.dept;


--***************************************************************************************************************************************--

salesman (sid, sname, location)
product (prodid, pdesc, price, category, discount)
sale (saleid, sid, sldate, amount)
saledetail (saleid, prodid, quantity)

Joining Conditions:
select sm.sid,sm.sname,sm.location,
p.prodid,p.pdesc,p.price,p.category,p.discount,
s.saleid,s.sid,s.sldate,s.amount,
sd.saleid,sd.prodid,sd.quantity
from salesman sm inner join sale s on sm.sid=s.sid
inner join saledetail sd on s.saleid=sd.saleid 
inner join product p on p.prodid=sd.prodid;


1. Display the sale id and sale date of sales made by salesmen working from London.
	Select s.saleid,s.sldate from salesman sm inner join sale s on sm.sid=s.sid where sm.location='London';
2. Display the salesmen id, salesmen name and location of those salesmen who are co-located.
	select sm1.sid, sm1.sname,sm1.location from Salesman sm1 inner join Salesman sm2 on sm1.location=sm2.location and sm1.sid<>sm2.sid
3. Display salesmen name and sale id of their sales. Consider SaleId as NULL if a Salesman has not made any sale.
	select distinct sm.sname,s.saleid from salesman sm left outer join sale s on sm.sid=s.sid left outer join saledetail sd on s.saleid=sd.saleid
4. Display the salesmen id, salesmen name, total sale amount and total discount by salesman who have made sales. Display the result in descending order of total sale amount and total discount.The sales amount for one sale can be calculated from price and quantity. The discount for one sale can be calculated from price, quantity and discount.
	select sm.sid,sm.sname,sum(sd.quantity*p.price) tamount, sum(sd.quantity*p.price* p.discount/100)  TDISCOUNT
	from salesman sm inner join sale s on sm.sid=s.sid
	inner join saledetail sd on s.saleid=sd.saleid 
	inner join product p on p.prodid=sd.prodid
	group by sm.sid,sm.sname order by 3 desc, 4 desc ;

5. For a given Sale Id display all products sold and id and name of salesman who made the sale.
	select sd.saleid,sd.prodid,sm.sid,sm.sname
	from salesman sm inner join sale s on sm.sid=s.sid
	inner join saledetail sd on s.saleid=sd.saleid 
	inner join product p on p.prodid=sd.prodid;

6. Display the salesmen id, salesmen name, total sale amount and total discount by salesman who have made sales. The total sale amount and total discount should appear as 0(Zero) for those Salesmen who were not able to make any sale.The sales amount for one sale can be calculated from price and quantity and discount
	select sm.sid,sm.sname,NVL(sum(sd.quantity*p.price),0) tamount, NVL(sum(sd.quantity*p.price* p.discount/100),0)  TDISCOUNT 
	from salesman sm 
	left outer join sale s on sm.sid=s.sid 
	left outer join saledetail sd on s.saleid=sd.saleid 
	left outer join product p on p.prodid=sd.prodid 
	group by sm.sid,sm.sname;
	
7. Display location, category and the total quantity sold location and category wise.
	select sm.location,p.Category,sum(sd.quantity) as "Total Quantity"
	from salesman sm inner join sale s on sm.sid=s.sid
	inner join saledetail sd on s.saleid=sd.saleid 
	inner join product p on p.prodid=sd.prodid
	group by sm.location,p.Category;
	
	
--***************************************************************************************************************************************--

Database structure	
Dept (Deptno, Dname, Loc)
Emp (Empno, Ename, Job, Mgr, Hiredate, Sal, Comm, Deptno)
Vehicle (Vehicleid, Vehiclename)
Empvehicle (Empno, Vehicleid) 

Joining Conditions:
select e.Empno, e.Ename, e.Job, e.Mgr, e.Hiredate, e.Sal, e.Comm, e.Deptno,
d.Deptno, d.Dname, d.Loc
from Emp e inner join Dept d on e.Deptno=d.Deptno

select v.Vehicleid,v.Vehiclename,
ev.Empno,ev.Vehicleid
from Vehicle v inner join Empvehicle ev on v.Vehicleid=ev.Vehicleid

select e.Empno, e.Ename, e.Job, e.Mgr, e.Hiredate, e.Sal, e.Comm, e.Deptno,
v.Vehicleid,v.Vehiclename,
ev.Empno,ev.Vehicleid
from  Emp e inner join Empvehicle ev on e.Empno=ev.Empno inner join 
Vehicle v on v.Vehicleid=ev.Vehicleid


Q. Display name and salary of the employees, who are drawing salary more than 2000. Along with that display the department names in which they are working.
	select e.Ename, e.Sal,d.Dname 	from Emp e inner join Dept d on e.Deptno=d.Deptno 	where e.sal>2000 

Q. Display the name of employees and their department names whose job designation is 'MANAGER'.
	select  e.Ename, d.Dname from Emp e inner join Dept d on e.Deptno=d.Deptno where e.job='MANAGER'

Q. List the department names that have more than one employee drawing salary more than 1500, working under it.
	select d.Dname from Emp e inner join Dept d on e.Deptno=d.Deptno and e.sal>1500 group by d.Dname\

Q. For each employee, identify the vehicle owned by them. Display ename and vehiclename for the same. Display name of employees even if they do not own any vehicle.
	select  e.Ename,v.Vehiclename from  Emp e left outer join Empvehicle ev on e.Empno=ev.Empno left outer join Vehicle v on v.Vehicleid=ev.Vehicleid

--***************************************************************************************************************************************--

Database structure
Computer (Compid, Make, Model, Myear)
Employee (Id, Ename, Doj, Salary, Bonus, Dept, Designation, Manager, Compid) 

select id, ename, doj, salary, bonus, dept, designation, manager, e.compid,
c.compid, make, model, myear
from employee e inner join computer c on e.compid=c.compid


Q. Display name and department of employees along with the model of the computer allocated to them. Employee details must be displayed with 'Not allocated' against the model column if computer is not allocated to the person.
	select ename, dept, NVL(model,'Not allocated') MODEL from employee e left outer join computer c on e.compid=c.compid

Q. Display id, name, department of employees along with model of computer allocated to them. The details for employee or computer should be displayed even if the employee has not been allocated a computer or a computer is not assigned to an employee.
	select ID,ENAME,DEPT,MODEL  from employee e full outer join computer c on e.compid=c.compid;

Q. Display id, name, manager id and manager name of those employees who are allocated a computer and whose manager is also allocated a computer. Also display the model of computer allocated to the employee and the manager

	select e.id, e.ename,e.manager MGRID,m.ename MGRNAME,ec.model E_MODEL,mc.model M_MODEL
	from employee e inner join employee m
	on e.manager=m.id
	inner join computer ec on e.compid=ec.compid
	inner join computer mc on m.compid=mc.compid



--***************************************************************************************************************************************--

salesman (sid, sname, location)
product (prodid, pdesc, price, category, discount)
sale (saleid, sid, sldate, amount)
saledetail (saleid, prodid, quantity)

Joining Conditions:
select sm.sid,sm.sname,sm.location,
p.prodid,p.pdesc,p.price,p.category,p.discount,
s.saleid,s.sid,s.sldate,s.amount,
sd.saleid,sd.prodid,sd.quantity
from salesman sm inner join sale s on sm.sid=s.sid
inner join saledetail sd on s.saleid=sd.saleid 
inner join product p on p.prodid=sd.prodid;

Q. Display the saleman id and names of salesmen who have not made any sales.
	select sm.sid,sm.sname from Salesman sm left outer join sale s on sm.sid=s.sid where s.sid is null 

Q. Display the product id, category and price of those products whose price is same.
	select p1.PRODID,p1.CATEGORY,p1.PRICE
	from product p1 inner join product p2
	on p1.price=p2.price and p1.Prodid<>p2.Prodid
	

--***************************************************************************************************************************************--
Database structure:
Item (Itemcode, Itemtype, Descr, Price, Reorderlevel, Qtyonhand, Category)
Quotation (Quotationid, Sname, Itemcode, Quotedprice, Qdate, Qstatus)
Orders (Orderid, Quotationid, Qtyordered, Orderdate, Status, Pymtdate, Delivereddate, Amountpaid, Pymtmode)
Retailoutlet (Roid, Location, Managerid)
Empdetails (Empid, Empname, Designation, Emailid, Contactno, Worksin, Salary)
Retailstock (Roid, Itemcode, Unitprice, Qtyavailable) 



Q. Display itemcode , item description and supplier name for all items for which quotations have been received from suppliers.
	select i.ITEMCODE,i.DESCR,q.SNAME from Item i inner join Quotation q on i.ITEMCODE=q.ITEMCODE;
	
Q. Display the custid and custname of those customers who are also employees. An employee and customer are considered same if their name and email id match.
	select CUSTID as "Customer Id", custname as "Customer Name" from Customer c inner join Empdetails e on c.custname=e.empname and c.emailid=e.emailid;

Q. The Manager of EasyShop would like to know details of those items for which quotations have been accepted. Display itemcode, item description, category and quotedprice for the same.
	select i.ITEMCODE,DESCR,CATEGORY,QUOTEDPRICE from item i inner join Quotation q on i.Itemcode=q.Itemcode and q.Qstatus='Accepted'

Q. The management of EasyShop would like to know the retail outlet ids and the details like description, type and retail unit price of items whose retail unit price is more than 1500.
	select ROID,DESCR,ITEMTYPE,UNITPRICE from Retailstock rs inner join item i on rs.itemcode=i.itemcode where rs.Unitprice>1500

Q. Display itemcode, supplier name and total quantity for the ordered items, whose total quantity ordered is greater than or equal to 100.
	select q.Itemcode,q.SNAME,sum(o.Qtyordered) as "TOTALQUANTITY" from Quotation q inner join Orders o on q.Quotationid=o.Quotationid group by q.Itemcode,q.SNAME having sum(o.Qtyordered) >=100

Q. The management wants the item code and description of the items whose item price matches with the quotation price for more than one quotation.
	select i.itemcode,i.descr from Item i inner join Quotation q on i.Itemcode=q.Itemcode and i.price=q.QUOTEDPRICE group by i.itemcode,i.descr having count(i.price)>1;

Q. EasyShop management wants to know sname and quotationid pertaining to the quotation of those orders, where there is a difference of less than or equal to 5 days between the delivered date and order date. Display unique rows.
	select distinct q.Sname, q.Quotationid from Quotation q inner join Orders o on q.Quotationid=o.Quotationid where Delivereddate-Orderdate<=5;

Q. Display manager name, category and category wise total quantity of items available in all retail outlet. Display manager name as 'No Manager' if no manager is appointed for the retail outlet.
	select nvl(e.empname,'No Manager') EMPNAME,i.CATEGORY,sum(rs.Qtyavailable) as TOTALQUANTITY
from Retailoutlet ro left outer join Retailstock rs on ro.Roid=rs.Roid left outer join Item i on i.Itemcode=rs.Itemcode left outer join Empdetails e on e.EMPID= ro.MANAGERID group by e.empname,i.CATEGORY

Q. Display retailoutlet location and customer type wise total quantity of items sold where customer's' gender is Male and item type is Apparels or Computer.
	select ro.LOCATION,c.Custtype, sum(p.Quantity) TOTALQUANTITY from Retailoutlet ro inner join Purchasebill p on ro.Roid=p.Roid inner join Customer c on c.Custid=p.Custid inner join item i on i.ITEMCODE=p.ITEMCODE where c.gender='M' and i.Itemtype in ('Apparels','Computer') group by ro.LOCATION,c.Custtype 

Q. Display itemcode, descr, billamount and custid for all items of type 'FMCG'. Display billamount and custid as zero if item is not sold.
	select i.ITEMCODE,i.DESCR,nvl(p.BILLAMOUNT,0) BILLAMOUNT , nvl(p.CUSTID,0) CUSTID from item i left outer join Purchasebill p  on i.Itemcode=p.Itemcode where i.Itemtype='FMCG'
Q. For each item, identify the stock availability in the retail outlet R1001. Display itemcode, desc and qtyavailable for the same. If there is no stock available for an item, display 'N.A.' for its quantity on available.
	select i.Itemcode,i.DESCR,nvl(to_char(rs.QTYAVAILABLE),'N.A.') QTYAVAILABLE from item i left outer join Retailstock rs on i.Itemcode= rs.Itemcode and rs.roid='R1001';

Q. Display the retailoutlet details like roid, location along with the employee details like empid and empname working in it. The details of retailoutlet should be displayed even if no employee is working in it.
	select e.EMPID,e.EMPNAME,ro.Roid,ro.Location from Empdetails e right outer join Retailoutlet ro on e.WORKSIN=ro.Roid

Q. Retrieve employee name, designation, and email id of those employees who work in the same retail outlet where George works. Do not display the record of George in the result.
	select distinct e3.EMPNAME,e3.DESIGNATION,e3.EMAILID from Empdetails e inner join Empdetails e2 on e.EMPNAME='George' and e.WORKSIN=e2.WORKSIN inner join Empdetails e3 on e.WORKSIN=e3.WORKSIN and e3.EMPNAME<>'George'

Q. Display the customer id and customer name of those customers who are co-located. Do not display the duplicate records/rows.
	select distinct c1.CUSTID, c1.CUSTNAME from Customer c1 inner join Customer c2 on c1.ADDRESS=c2.ADDRESS and c1.Custid <> c2.Custid
	
Q. Identify the quotations submitted by the supplier for each item of type 'Computer' or 'FMCG'. Display itemtype, description and quotationid for the same. If the quotation has not been received for an item, display 'No quotation received' for quotationid.
	select i.Itemtype, i.Descr,nvl(q.Quotationid,'No quotation received') as "Available Quotation number" from item i left outer join Quotation q on i.Itemcode=q.Itemcode where i.Itemtype in ('Computer','FMCG')

Q. Display custname and billamount for those customers who have shopped for more than 5000.
	select CUSTNAME,BILLAMOUNT from Purchasebill p inner join Customer c on p.Custid=c.Custid where p.BILLAMOUNT>5000;

Q. Retrieve the retailoutlet and the number of items stocked for that outlet
	select ROID, count(ITEMCODE) as "Number of items" from Retailstock group by ROID
	
Q. For items of type Apparels, identify quantity available at each retail outlet location. Display itemcode, item description, quantity available and retail outlet location.
	select i.itemcode, i.DESCR, ro.Location, sum(rs.Qtyavailable) QTYAVAILABLE from Retailoutlet ro inner join Retailstock rs on ro.roid=rs.roid inner join Item i on rs.itemcode=i.itemcode and i.itemtype='Apparels' group by i.itemcode, i.DESCR, ro.Location

Q. Identify the quotations submitted for all items. Display itemcode, item description and quotationid for the same. If the item does not have any quotation, display 'No quotation received' for quotationid.
	select i.itemcode, i.descr, nvl(q.Quotationid,'No quotation received') QUOTATIONID 	from item i left outer join Quotation q 	on i.itemcode=q.ITEMCODE;
	
Q. The management of EasyShop would like to know the qtyavailable for items at retail outlets. Display itemcode, description, qtyavailable and roid for those items.
	select i.ITEMCODE,i.DESCR,rs.ROID,rs.QTYAVAILABLE from item i inner join Retailstock rs on i.ITEMCODE=rs.ITEMCODE

Q. The management of EasyShop would like to know the description, type of items and amount billed to customers for retail outlet 'R1001'.
	select i.DESCR, i.ITEMTYPE, p.BILLAMOUNT from item i inner join Purchasebill p on i.ITEMCODE=p.ITEMCODE and p.roid='R1001';

Q. Retrieve the customer id, customer name, and customer type of those customers who are of the same customer type as that of customer id 2004. Do not display the record of customer id 2004 in the result.
	select c3.CUSTID,c3.CUSTNAME,c3.CUSTTYPE from Customer c1 inner join Customer c2 on c1.CUSTID=2004 and c1.custid=c2.custid inner join customer c3 on c1.CUSTTYPE=c3.CUSTTYPE and c3.CUSTID<>2004 

Q .Identify the quotations submitted by the supplier for each item of type 'Computer' or 'FMCG'. Display itemtype, description and quotationid for the same. If the quotation has not been received for an item, display 'No quotation received' for quotationid.
	select i.ITEMTYPE,i.DESCR,nvl(q.Quotationid,'No quotation received') as "Available Quotation number" from item i left outer join  Quotation q on i.Itemcode=q.Itemcode where i.Itemtype in ('Computer','FMCG')
	
--***************************************************************************************************************************************--
SUB-Query:
--***************************************************************************************************************************************--

Database structure
Salesman (Sid, Sname, Location)
Product (Prodid, Pdesc, Price, Category, Discount)
Sale (Saleid, Sid, Sldate, Amount)
Saledetail (Saleid, Prodid, Quantity) 


Q. Display the sale id and date for most recent sale.
	select Saleid,SLDATE from Sale where Sldate = (select max(SLDATE) from Sale )
	
Q. Display the names of salesmen who have made at least 2 sales.
	select sname from salesman where sid in(select SID from Sale group by SID having count(SID)>1)

Q. Display the product id and description of those products which are sold in minimum total quantity.
	select PRODID,Pdesc from Product where Prodid in (select PRODID from Saledetail group by PRODID having sum(Quantity) = (select min(sum(Quantity)) from Saledetail group by PRODID))

https://lex.infosysapps.com/viewer/rdbms-hands-on/lex_auth_01273624649120153645?collectionId=lex_auth_0127673005629194241&collectionType=Course&viewMode=RESUME
Exercise 62
Q. Display SId, SName and Location of those salesmen who have total sales amount greater than average sales amount of all the sales made. Amount can be calculated from Price and Discount of Product and Quantity sold.
	select sid,sname,location from salesman where sid in(select s.sid from sale s inner join saledetail sd on s.saleid=sd.saleid inner join product p on p.prodid=sd.prodid group by s.sid having sum(sd.Quantity*p.Price*(1- p.Discount/100))> 	(select avg(sum(sd.Quantity*p.Price*(1- p.Discount/100))) avg from Saledetail sd inner join Product p on p.Prodid=sd.Prodid group by sd.saleid))

Q. Display the product id, category, description and price for those products whose price is maximum in each category.
	select p.PRODID,p.CATEGORY,PDESC,p.PRICE from Product p inner join (select CATEGORY, max(Price) price from Product group by CATEGORY) x on p.Category=x.Category and p.price=x.price

Q. Display the names of salesmen who have not made any sales.
	select sname from Salesman where sid not in (select sid from Sale )

Q. Display the names of salesmen who have made at least 1 sale in the month of Jun 2015.
	select Sname from Salesman where sid in  (select sid from Sale where to_char(Sldate,'Mon YYYY')='Jun 2015')

Q. Display SId, SName and Location of those salesmen who have total sales amount greater than average total sales amount of their location calculated per salesman. Amount can be calculated from Price and Discount of Product and Quantity sold.
	select sm.sid, sname, location from salesman sm join sale s on sm.sid=s.sid join saledetail sd s.saleid=sd.saleid join product p on sd.prodid=p.proid group by sm.sid, sname, location having sum(price*quantity*(1-discount/100)) > (select avg(sum(price*quantity*(1-discount/100))) from sale s1 join salesman sm1 on s1.sid=sm1.sid join saledetails sd1 on s1.saleid=sd1.saleid join product p1 on sd1.prodid=p1.prodid where sm.location=sm1.location group by s1.sid);

--***************************************************************************************************************************************--
Item (Itemcode, Itemtype, Descr, Price, Reorderlevel, Qtyonhand, Category)
Quotation (Quotationid, Sname, Itemcode, Quotedprice, Qdate, Qstatus)
Orders (Orderid, Quotationid, Qtyordered, Orderdate, Status, Pymtdate, Delivereddate, Amountpaid, Pymtmode)
Retailoutlet (Roid, Location, Managerid)
Empdetails (Empid, Empname, Designation, Emailid, Contactno, Worksin, Salary)
Retailstock (Roid, Itemcode, Unitprice, Qtyavailable)
Customer (Custid, Custtype, Custname, Gender, Spouse, Emailid, Address)
Purchasebill (Billid, Roid, Itemcode, Custid, Billamount, Billdate, Quantity)


Q. Identify the items which are purchased by customers of retail outlets. Display itemcode, itemtype, descr and category of those items. Display unique rows.
	select distinct i.ITEMCODE,i.ITEMTYPE,i.DESCR,i.CATEGORY from Item i inner join Purchasebill p on i.Itemcode=p.Itemcode
	
Q. Identify the item details that have the least quoted price with the quotation status as 'Rejected'. Display itemcode, itemtype, descr and category of those items.
	select ITEMCODE,ITEMTYPE,DESCR,CATEGORY from Item where Itemcode in (select distinct ITEMCODE from Quotation where Quotedprice = (select min(Quotedprice) from Quotation where Qstatus='Rejected') and Qstatus='Rejected');

Q. The management would like to know the details of the items which has maximum quoted price amongst the quotations that have status as 'Closed' or 'Rejected'. Display itemcode and descr of those items.
	select ITEMCODE,DESCR from Item where Itemcode in (select distinct ITEMCODE from Quotation where Quotedprice =(select max(Quotedprice) from Quotation where Qstatus in ('Closed','Rejected')) and Qstatus in ('Closed','Rejected'))

Q. Identify the item having second highest price. Display itemcode, descr and price of those items.
	select ITEMCODE,DESCR,PRICE from item where price = (select max(price) from item where itemcode not in (select Itemcode from Item where price=(select max(PRICE) from Item)))

Q. Display the itemcode, descr and qdate for those items which are quoted below the maximum quoted price on the same day.
	select i.itemcode, descr, qdate from item i join quotation q on i.itemcode=q.itemcode where quotedprice<(select max(quotedprice) from quotation q1 where q1.qdate=q.qdate);

Q. Identify purchase bills in which the bill amount is less than or equal to the average bill amount of all the items purchased in the same retail outlet. Display billid and itemcode for the same.
	select billid, itemcode from purchasebill p inner join retailoutlet r on r.roid=p.roid where billamount<=(select avg(billamount) from purchasebill p1 where p1.roid=r.roid);

Q. Identify the supplier who has submitted the quotation for an item with the quoted price, less than the maximum quoted price submitted by all other suppliers, for the same item.Display sname, itemcode and item description for the identified supplier. Do not display duplicate records.
	select distinct sname, i.itemcode, descr from item i join quotation q on i.itemcode=q.itemcode where quotedprice<(select max(q1.quotedprice) from quotation q1 where (i.itemcode=q1.itemcode and q.sname<>q1.sname) group by q.itemcode, q.sname);

Q. The payroll department requires the details of those employees who are getting the highest salary in each designation. Display empid, empname, designation and salary as per the given requirement.
	select e.EMPID,e.EMPNAME,e.DESIGNATION,e.SALARY from Empdetails e inner join (select DESIGNATION, max(salary) sal from Empdetails  group by DESIGNATION) e1 on e.DESIGNATION=e1.DESIGNATION and e.SALARY=e1.sal

Q. Display the customer id and customer name of those customers who have not purchased at all from any retail outlet.
	select distinct c.CUSTID,c.custname from Customer c left join Purchasebill p on c.CUSTID=p.CUSTID where p.roid is null

Q. Display the customer id and customer name of those customers who have purchased at least once from any retail outlet.
	select distinct c.CUSTID,c.custname from Customer c inner join Purchasebill p on c.CUSTID=p.CUSTID


--***************************************************************************************************************************************--
Database structure
Dept (Deptno, Dname, Loc)
Emp (Empno, Ename, Job, Mgr, Hiredate, Sal, Comm, Deptno)
Vehicle (Vehicleid, Vehiclename)
Empvehicle (Empno, Vehicleid) 

Q. Display the ename and job of the employees who own vehicle.
	select ename,job from Emp e inner join Empvehicle ev on ev.Empno=e.Empno;

Q. Display the name of the employee who is drawing maximum salary.
	select ename from emp where sal= (select max(sal) from emp)

Q. Identify the vehicle which is purchased by the maximum number of employees. Display empno and ename of the employees who have purchased the identified vehicles.
	select empno,ename from emp where empno in (select Empno from Empvehicle where Vehicleid =(select Vehicleid from Empvehicle group by Vehicleid having count(*)= (select max(count(*)) from Empvehicle group by Vehicleid)))

Q.Display the empno and ename of all those employees whose salary is greater than the average salary of all employees working in their departments.
	select empno,ENAME from emp e1 inner join (select Deptno,avg(sal) avg from Emp group by Deptno) e2 on e1.Deptno=e2.Deptno and e1.Sal>e2.avg













