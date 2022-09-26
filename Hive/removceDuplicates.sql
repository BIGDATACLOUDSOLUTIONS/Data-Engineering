create table CONTROLTABLE (sourcename string,Primarykeycolumn string,extractdatecolumn string,Description string);


insert into CONTROLTABLE values('tab1', 'a', 'ext1', 'fact');
insert into CONTROLTABLE values('tab2', 'b', 'ext2', 'dimension');
insert into CONTROLTABLE values('tab3', 'c', 'ext3', 'dimension');


----------------

create table tab1 (a int,first_name string,email string,ext1 string)
row format delimited
fields terminated by ',';


drop table tab1;

load data local inpath /home/cloudera/Desktop/MOCK_DATA.csv into table tab1;


Queries:

with result as(
SELect a, first_name, email, ext1, row_number() over(partition by a order by ext1 desc) as row_num from tab1
)select * from result where row_num = 1;
