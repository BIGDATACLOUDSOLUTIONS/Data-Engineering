basepath=/home/maria_dev/hive/practice_sql/prepare_data/datafiles
Computer=CompAndEmp
Employee=CompAndEmp
Salesman=SalesRelated
Product=SalesRelated
Sale=SalesRelated
Saledetail=SalesRelated
Item=ItemAndRetailOutlet
Quotation=ItemAndRetailOutlet
Orders=ItemAndRetailOutlet
Retailoutlet=ItemAndRetailOutlet
Empdetails=ItemAndRetailOutlet
Retailstock=ItemAndRetailOutlet
Customer=ItemAndRetailOutlet
Purchasebill=ItemAndRetailOutlet
Dept=VehiclesRelated
Emp=VehiclesRelated
Vehicle=VehiclesRelated
Empvehicle=VehiclesRelated
tableNames="Computer Employee Salesman Product Sale Saledetail Item Quotation Orders Retailoutlet Empdetails Retailstock Customer Purchasebill Dept Emp Vehicle Empvehicle"

echo "use hivesql;" >>load.hql
for t in $tableNames
do
echo "load data local inpath '$basepath/${!t}/$t.csv' overwrite into table $t;">> load.hql
done
hive -f create_table_stt.hql
hive -f load.hql
rm -rf load.hql