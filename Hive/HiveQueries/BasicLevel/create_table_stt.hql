create database if not exists hivesql;
use hivesql;


create table if not exists Computer(
Compid int,
Make string,
Model string,
Myear int
)
row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Employee(
id int,
ename string,
doj DATE,
salary int,
bonus int ,
dept string,
designation string,
manager int,
compid int
)
row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");

create table if not exists Salesman (
Sid int,
Sname string,
Location string
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Product (
Prodid int,
Pdesc string,
Price int,
Category string,
Discount int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Sale (
Saleid int,
Sid int,
Sldate date, 
Amount int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");

  
create table if not exists Saledetail (
Saleid int,
Prodid int ,
Quantity int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Dept (
Deptno int,
Dname string,
Loc string
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Emp (
Empno int,
Ename string,
Job string,
Mgr int,
Hiredate DATE,
Sal int,
Comm string,
Deptno int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");

  
create table if not exists Vehicle (
Vehicleid int,
Vehiclename string
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");

 
create table if not exists Empvehicle (
Empno int,
Vehicleid int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");



create table if not exists Item (
Itemcode string,
Itemtype string, 
Descr string, 
Price int, 
Reorderlevel int, 
Qtyonhand int, 
Category string
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Quotation (
Quotationid string,
Sname string,
Itemcode string,
Quotedprice int,
Qdate DATE,
Qstatus string
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");

 
create table if not exists Orders (
Orderid string, 
Quotationid string, 
Qtyordered int, 
Orderdate DATE, 
Status string, 
Pymtdate DATE, 
Delivereddate DATE, 
Amountpaid int, 
Pymtmode string
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");

 
create table if not exists Retailoutlet (
Roid string,
Location string,
Managerid int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Empdetails (
Empid int,
Empname string,
Designation string,
Emailid string,
Contactno int,
Worksin string,
Salary int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");

create table if not exists Retailstock (
Roid string,
Itemcode string,
Unitprice int,
Qtyavailable int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");


create table if not exists Customer (
Custid int, 
Custtype string,
Custname string,
Gender string,
Spouse string,
Emailid string,
Address string
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");
 

create table if not exists Purchasebill (
Billid string,
Roid string,
Itemcode string,
Custid int,
Billamount int,
Billdate DATE,
Quantity int
)row format delimited
fields terminated by ','
tblproperties ("skip.header.line.count"="1");