Scenario 1:
very large dataset:
you have to partiton the data based on some column and write it hdfs and each partiton should have only one file

Hint: Usually not possible because file is written by multiple executor. 
One way is use colease, Second may be repartion on partitioning column as key, third might be write merger application

