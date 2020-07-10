# language: ru
# Образец сценария для отправки и получения данных из БД
@SampleSql

Функция: Sample SQL Scenario

  Сценарий: SampleSql
    #1
    Дано создать таблицу "PRODUCTS"
    #2
    И выполнить запрос "INSERT INTO PRODUCTS (PRODUCT, PRICE) VALUES ('BREAD', 30), ('CHEESE ', 45), ('WATER', 36)"
    #3
    И получить информацию из таблицы "PRODUCTS"
    #4
    И удалить запись "BREAD" в таблице "PRODUCTS"
    #5
    И удалить таблицу "PRODUCTS"
