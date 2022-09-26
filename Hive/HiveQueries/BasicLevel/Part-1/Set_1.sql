Salesman (Sid, Sname, Location)
Product (Prodid, Pdesc, Price, Category, Discount)
Sale (Saleid, Sid, Sldate, Amount)
Saledetail (Saleid, Prodid, Quantity) 

Write a query to list all products from the Product table shown below.
Write a query to list product id, price and category for all products from the Product table.
Write a query to list all product categories from the Product table.


Patter Question:
ID	ENAME	DOJ	SALARY	BONUS	DEPT	DESIGNATION
1	James Potter	01-Jun-14	75000	1000	ICP	PM
2	Ethan McCarty	01-Feb-14	90000	1200	ETA	PM
3	Emily Rayner	01-Jan-14	25000	100	ETA	SE
4	Jack Abraham	01-Jul-14	30000	NULL	ETA	SSE
5	Ayaz Mohammad	01-Apr-14	40000	NULL	ICP	TA


Displays those rows where name begins with character 'E' followed by any number of characters.
Displays those rows where name ends with character 'r'.
Displays those rows where date ends with 14.
Displays those rows where date contains any 2 characters followed by '-' and then any 3 characters followed by '-' and then any 2 characters.
Displays those rows where name contains second character as 'a' followed by any number of characters.
----------------
Write a query to display product id, description, category and discount of all Apparel products from the Product table.
Write a query to display product id, description, category and discount of all products that do not have any description.
Write a query to display product id, description, category and discount of all Apparel products that have discount more than 5 percent.


Function Based:
1. Write a query to display Product Id, Product Description and Category of those products whose category name is electronics. Do case insensitive comparison.
	select PRODID, PDESC,CATEGORY from Product where lower(CATEGORY) ='electronics' ;

2. Write a query to display the product id, first five characters of product description and category of those products.
	select Prodid, substr(Pdesc,1,5) as PDESC_FIVE,CATEGORY from Product ;

3. Write a query to display the number of sales that were made in the last 40 months.
	select count(Saleid) SALE_COUNT from Sale where MONTHS_BETWEEN(sysdate,Sldate) <=40  ;

4. Write a query to display the product description and discount for all the products. Display the value 'No Description' if description is not having any value i.e. NULL.
	select NVL(PDESC,'No Description') PDESC,DISCOUNT from Product ;

5. Display product id, category, price and discount of all products in descending order of category and ascending order of price.
	select PRODID,CATEGORY,PRICE,DISCOUNT from Product order by category desc,price asc;
	
6. Display product id, category and discount of the products which belongs to the category ‘Sports’ or ‘Apparel’ in ascending order of category and discount.
	select PRODID,CATEGORY,DISCOUNT from Product where CATEGORY in ('Sports','Apparel') order by category ,discount;

7. Display month of sale and number of sales done in that month sorted in descending order of sales.
	select to_char(SLDATE,'Month') MONTH,count(*) NUMBER_SALE from Sale group by to_char(SLDATE,'Month') order by NUMBER_SALE desc ;
	
8. Display product id and total quantity for products that have been sold more than once. Consider only those sale instances when the quantity sold was more than 1.
	select PRODID,sum(Quantity) QTY_SOLD from saledetail where QUANTITY>1 group by PRODID having count(PRODID)>1;


9. A discount of 22.5% being offered on all sports category item's' unit price, Write a query to display product id, product description, existing unit price as "Old_Price" and discounted price as "New_Price" for sports category item. Round off the discounted price to two decimal places.
	select PRODID,PDESC,Price as "Old_Price",round(Price*.775,2) as "New_Price" from Product where Category='Sports';

10. Write a query to display the sale id, no. of months between the saledate and today's' date as "MONTH_AGED" for all the sales. Show positive values for no. of months and round it to 1 decimal places.
	select SALEID, round(MONTHS_BETWEEN(sysdate,Sldate),1) MONTH_AGED from Sale 

11. Write a query to display the average price (rounded to 2 decimal places) as Avg, min price as Min, max price as Max and total number of products available as Total.
	select round(avg(Price),2) as "Avg", min(Price) as "Min", max(Price) as "Max", count(Price) as "Total" from Product;

12. Display the concatenated value of Salesman Name and his location. Use constant string ' is from ' for concatenation (contains single space at beginning and end).
	select concat(Sname,concat(' is from ', Location)) RESULT from Salesman 

13. Display the full Month name from string representation of date stored in "Jan/10/2015" format. Also display the amount 2,50,000 by converting it as 250000 as "Amount". Use dual table to perform above operation.
	select to_char(to_date('Jan/10/2015','Mon/dd/yyyy'),'Month') as MONTH, TO_NUMBER('2,50,000','9,99,999') as AMOUNT from dual;

14. Display the product id, description and price of products in decreasing order of price and product id.
	select PRODID,PDESC,PRICE from Product order by price desc,PRODID desc

15. Display product id, description and price of all products in ascending order of description.
	select PRODID,PDESC,PRICE from Product order by description asc;
	
16. Display the location and total number of Salesmen in each location.
	select Location, count(*) NUMBER_SMAN  from Salesman group by Location ;

17. Display categories that have more than 1 product.
	select CATEGORY from Product group by CATEGORY having count(CATEGORY) >1;


