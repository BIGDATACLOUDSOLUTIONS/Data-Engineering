Database structure
Dept (Deptno, Dname, Loc)
Emp (Empno, Ename, Job, Mgr, Hiredate, Sal, Comm, Deptno)
Vehicle (Vehicleid, Vehiclename)
Empvehicle (Empno, Vehicleid) 

1. Identify the average salary of the jobs 'MANAGER' and 'ANALYST'. Display job, average salary if the identified average salary is more than 1500
	select JOB, avg(SAL) as "Average Salary"from emp where job in ('MANAGER','ANALYST') group by JOB;
	
2. Display the job, deptno and average salary of employees belonging to department 10 or 20 and their salary is more than 2000 and average salary is more than 2500.
select JOB,DEPTNO, avg(sal) as "AVGSALARY" from Emp where DEPTNO in (10,20) and sal >2000 group by JOB,DEPTNO having avg(sal) >2500;
