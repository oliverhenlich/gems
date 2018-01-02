# Mysql / Mariadb 

## Information

### Sizes
* Total database sizes
~~~~
SELECT table_schema                                               "DB Name",
   Round(Sum(data_length + index_length) / 1024 / 1024 / 1024, 1) "DB Size in GB"
FROM   information_schema.tables
GROUP  BY table_schema;
~~~~

~~~~
SELECT table_schema                                               "DB Name",
       table_name                                                 "Table",
       table_rows                                                 "Row count",
       Round((data_length + index_length) / 1024 / 1024 / 1024, 2) "Size in GB"
FROM   information_schema.tables
where table_rows > 100000
ORDER BY table_schema, table_name;
~~~~

## Mariadb slow query log

~~~~
SET GLOBAL long_query_time = 10; 
SET GLOBAL LOG_QUERIES_NOT_USING_INDEXES = ON;
SET GLOBAL log_output = 'FILE,TABLE';

SET GLOBAL slow_query_log = 'ON';

FLUSH SLOW LOGS ;
FLUSH TABLES; 

SHOW GLOBAL VARIABLES LIKE '%slow%';
SHOW GLOBAL VARIABLES LIKE 'log_queries_not_using_indexes';
SHOW GLOBAL VARIABLES LIKE 'long_query_time';
~~~~

Query it with
~~~~
SELECT * FROM mysql.slow_log;
~~~~

Remember to turn it off again with
~~~~
SET GLOBAL slow_query_log = 'OFF';
FLUSH SLOW LOGS ;
~~~~


## Mariadb general log

**DO NOT LEAVE THIS ON!**

~~~~
SET GLOBAL general_log = 'ON';
SHOW VARIABLES LIKE "general_log%";

# DON'T FORGET
SET GLOBAL general_log = 'OFF';
~~~~

Query it with
~~~~
SELECT * FROM mysql.general_log;
~~~~


## Meta-Data

Table sizes
~~~~
  select TABLE_NAME, TABLE_ROWS, DATA_LENGTH from information_schema.tables where table_schema='OLAP_US_STAGING_2' order by TABLE_ROWS, DATA_LENGTH;
~~~~
