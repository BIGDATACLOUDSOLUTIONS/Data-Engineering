Item  (Itemcode, Itemtype, Descr, Price, Reorderlevel, Qtyonhand, Category)
Quotation (Quotationid, Sname, Itemcode, Quotedprice, Qdate, Qstatus)
Orders (Orderid, Quotationid, Qtyordered, Orderdate, Status, Pymtdate, Delivereddate, Amountpaid, Pymtmode)
Retailoutlet (Roid, Location, Managerid)
Empdetails (Empid, Empname, Designation, Emailid, Contactno, Worksin, Salary)
Retailstock (Roid, Itemcode, Unitprice, Qtyavailable)
Customer (Custid, Custtype, Custname, Gender, Spouse, Emailid, Address)
Purchasebill (Billid, Roid, Itemcode, Custid, Billamount, Billdate, Quantity) 

1. Display descr and price of different sizes of all 'Hard disk'.
	select DESCR,PRICE from Item where DESCR like '%Hard disk%';

2. Display quotationid, sname, itemcode, quotedprice, qdate, qstatus of those quotations which are not accepted.
	select * from Quotation where Qstatus <> 'Accepted';

3. Retrieve the designation and salary of all 'Manager' and 'Billing Staff'.
	select DESIGNATION,SALARY from Empdetails where DESIGNATION in ('Manager','Billing Staff');

4. Retrieve the roid and location which does not have a Manager.
	select ROID,LOCATION from Retailoutlet where MANAGERID is null ;

5. Retrieve the orderid, quotationid and status of those orders where order is placed between the dates '1-Dec-2014' and '1-Jan-2015'.
	select ORDERID,QUOTATIONID,STATUS from Orders where Orderdate between '1-Dec-2014' and '1-Jan-2015' ;

6. Retrieve the itemcode, descr and unit price of the items which have 'Shirt' or 'Skirt' in their desc and their category is 'B'.
	select ITEMCODE,DESCR,PRICE from Item where category = 'B' and (DESCR like '%Shirt%' or DESCR like '%Skirt%')

7. Retrieve the designation and salary of employees without any duplication of data.
	select distinct designation,salary from Empdetails ;
	
8. Retrieve itemcode, description and price for all items.
	select ITEMCODE,DESCR,PRICE from Item ;

9. Retrieve the quotation id and sname of the quotations which have been either 'Accepted' or 'Rejected'.
	select QUOTATIONID,SNAME from Quotation where Qstatus in ('Accepted','Rejected');

10. Retrieve item id, item description and price for items whose names have 'r' as the second character.
	select ITEMCODE,DESCR,PRICE from Item  where DESCR like '_r%'

13. Retrieve the different item types.
	select distinct ITEMTYPE from Item;

14. Retrieve the order details like orderid, quotationid, status, pymtdate for those orders where payments are not received.
	select ORDERID,QUOTATIONID,STATUS,PYMTDATE from Orders where STATUS='Ordered' and PYMTDATE is null;

15. Retrieve the different item types and category of the items.
	select distinct ITEMTYPE,category from Item;

16. The management wants to increase salary of all employees by 10%. Write a query to display empid, current salary, increased salary and 	 incremented amount.
	select empid,Salary as "Current Salary", Salary*1.1 as "New Salary", salary*0.1 as "Incremented Amount" from Empdetails;


17. Retrieve quotationid, qdate and quotedprice for quotations that are quoted in 1400 to 2150 range (not inclusive of these values).
	select QUOTATIONID,QDATE,QUOTEDPRICE from Quotation where Quotedprice > 1400 and Quotedprice< 2150  ;

18. Retrieve the itemtype, descr and unit price of those items whose price is more than 4000.
	select ITEMTYPE,DESCR,PRICE from Item where PRICE>4000;


19. Retrieve the designation and salary of all 'Manager' and 'Billing Staff' who have salary in the range of 2500 to 5000 (both inclusive).
	select DESIGNATION,SALARY from Empdetails where DESIGNATION in ('Manager', 'Billing Staff' ) and salary between 2500 and 5000 ;

20. Display the month and number of quotations received in each month.
	select to_char(Qdate,'Month') MONTH,count(Quotationid) QUOTATIONCOUNT from Quotation group by to_char(Qdate,'Month') ;


21. The management of EasyShop would like to classify the items as cheap, affordable, expensive and very expensive. The classification is done if item unit price is between 0 and 499 then 'Cheap', if between 500 and 1999 then 'Affordable', if between 2000 and 4999 then 'Expensive' and if price is more than or equal to 5000 then 'Very Expensive'. Write a query to display the itemtype and classification of items. Display unique rows sorted by itemtype and classification in ascending order.
	select distinct ITEMTYPE,
	case
	when Price between 0 and 499 then 'Cheap'
	when Price between 500 and 1999 then 'Affordable'
	when Price between 2000 and 4999 then 'Expensive'
	else 'Very Expensive'
	end as CLASSIFICATION
	from Item order by itemtype , CLASSIFICATION  ;

22. Retrieve the orderid and the number of monthsbetween pymtdate and orderdate for all orders where the number of months is more than 0.1 and the payment has been done. Round off the number of months up to two decimal places.
	select ORDERID,round(MONTHS_BETWEEN(PYMTDATE,ORDERDATE),2) as "No of Months" from Orders where AMOUNTPAID is not null and round(MONTHS_BETWEEN(PYMTDATE,ORDERDATE),2) >0.1;


23. The salary of managers has been increased by 20%. Retrieve the empid, existing salary as "Current Salary", increased salary as "New Salary" and the difference of existing and increased salary as "Incremented Amount". Round off the increased salary up to two decimal places.
	select Empid, salary as "Current Salary", round(salary*1.2,2) as "New Salary",round(salary*0.2,2) as "Incremented Amount" from Empdetails where DESIGNATION= 'Manager';


24. Display the itemcode of those items where the difference of quantity on hand and reorder level is more than 50. Convert the result of difference to positive values.
	select Itemcode from Item where abs(Qtyonhand-Reorderlevel) >50


25. Retrieve the itemcode and average of quantity available in retail outlets where the average of quantity available is less than 75.
	select Itemcode, round(avg(Qtyavailable),2) as "Average Quantity" from  Retailstock  group by Itemcode having round(avg(Qtyavailable),2) <75;
	
26.Display pymtmode, and total number of payments for those payments which were paid before the year 2015 and total number of payments should be more than 1.
	select PYMTMODE,count(PYMTMODE) PYMTCOUNT from Orders where to_number(to_char(PYMTDATE,'yyyy'),'9999')  <2015 group by PYMTMODE having count(PYMTMODE) >1 ;

27. Retrieve the Supplier Name for those quotations whose average quoted price for all quotations quoted by him is more than 500 and the quotation status is closed. Also display average quoted price
	select SNAME,avg(QUOTEDPRICE) as "Average quoted price" from Quotation where QSTATUS= 'Closed' group by SNAME having avg(QUOTEDPRICE)> 500

28. Retrieve the itemtype, category of item, and average price rounded to two decimal places of FMCG, Computer type items. The average price must be less than 2000.
	select Itemtype,CATEGORY, round(avg(Price),2) as "Average item price" from Item where Itemtype in ('FMCG','Computer') group by Itemtype,CATEGORY having  round(avg(Price),2) <2000;
	

29. Salary hikes are being given to all employees of EasyShop based on their role. The percentage increase is as given below. Write a query to display EmpId, EmpName, Salary and increased salary.
Designation(Role)	Hike in %
Administrator 	10
Manager 	5
Billing Staff 	20
Security 	25
Others 	2

	select EMPID, EMPNAME,SALARY,
	case
	when Designation='Administrator' then SALARY*1.1
	when Designation='Manager' then SALARY*1.05
	when Designation='Billing Staff' then SALARY*1.2
	when Designation='Security' then SALARY*1.25
	else SALARY*1.02
	end as INCREASEDSALARY
	from Empdetails 



30. The management of EasyShop would like to classify the salary of employees as Class 3, Class 2 and Class 1. The classification is done as if salary is less than 2500 then the class is 'Class 3' , if between 2500 and 5000 then 'Class 2', and if salary is more than 5000 then 'Class 1'. Write a query to display EmpId, Salary and classification of salary.
	select EMPID,SALARY,
	case
	when salary<2500 then 'Class 3'
	when salary between 2500 and 5000 then 'Class 2'
	else 'Class 1'
	end as SALGRADE
	from Empdetails 


31. For a discount of 25.5% being offered on all FMCG item's' unit price, display item code, existing unit price as "Old Price" and discounted price as "New Price ". Round off the discounted price to two decimal values.
	select ITEMCODE,Price as "Old Price",round(Price*.745,2) as "New Price" from Item where ITEMTYPE='FMCG';

32. Retrieve the employee id, employee name of billing staff and the retail outlet where they work. Perform a case insensitive search.
	select EMPID,EMPNAME,Worksin from Empdetails where Designation ='Billing Staff' ;


33. Retrieve the order id, order status and payment mode of all the orders. Display 'Payment yet not done' against payment mode column if the payment has not been done.
	select ORDERID,STATUS,NVL(PYMTMODE,'Payment yet not done') PYMTMODE from Orders ;

34. Retrieve the description of items which have more than 15 characters.
	select DESCR from Item where length(DESCR)>15

35. Display numeric part of retail outlet id
	select substr(ROID,2,length(ROID)) NUMERICROID from Retailoutlet 


36. Display current date as 'Mon/DD/YYYY Day'
	select to_char(sysdate,'Mon/DD/YYYY Day') CURRENTDATE from dual;


37. Retrieve the total number of orders made and the number of orders for which payment has been done.
	select count(ORDERID) TOTALORDERSCOUNT,count(PYMTMODE) PAIDORDERSCOUNT from Orders ;


38. Retrieve the order id and the number of days between order date and payment date for all orders. The number of days should be displayed as positive values.
	select ORDERID, round(abs(MONTHS_BETWEEN( ORDERDATE,Pymtdate)* 31),2) NOOFDAYS from Orders ;


39. Retrieve the total number of different item types available.
	select count(distinct Itemtype) NOOFITEMTYPES from Item

40. Retrieve the maximum salary, minimum salary, total salary and average salary of employees.
	select max(Salary) as MAXSAL, min(Salary) as MINSAL, sum(Salary) as TOTALSAL, avg(Salary) as AVGSAL from Empdetails; 

41. Retrieve the name of supplier who has quoted greater than 1000 and the quotation status is closed and average of quoted price is less than 4500. Also display average quoted price.
	select SNAME,avg(QUOTEDPRICE) as "Average quoted price" from Quotation  where QSTATUS= 'Closed' and QUOTEDPRICE>1000 group by SNAME

42. Retrieve the total number of items available in warehouse
	select count(ITEMCODE) NOOFITEMS from Item ;	

43. Retrieve the order id and the number of months between order date and payment date for all orders.
	select ORDERID, MONTHS_BETWEEN(Orderdate,Pymtdate) as "No of Months" from Orders 

