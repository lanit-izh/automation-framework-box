package steps;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.И;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SqlSteps extends BaseSteps {
    private static Logger LOGGER = LoggerFactory.getLogger(SqlSteps.class);

    private String path = "src/main/resources/database.properties";

    private String url;
    private String username;
    private String password;

    /*Блок получения данных для подключения к БД. Параметры подключения нужно менять в файле database.properties*/
    {
        Properties props = new Properties();
        
        try {
            props.load(new FileInputStream(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        url = props.getProperty("datasource.url");
        username = props.getProperty("datasource.username");
        password = props.getProperty("datasource.password");
    }

    @Дано("создать таблицу {string}")
    public void createTable(String name) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            LOGGER.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            String createRequest = "CREATE TABLE " + name + "(\n" +
                    "\tID serial PRIMARY KEY,\n" +
                    "\tPRODUCT VARCHAR (255) NOT NULL,\n" +
                    "\tPRICE integer NOT NULL\n" +
                    ");";
            statement.execute(createRequest);
            LOGGER.info(() -> "Таблица создана!");
        } catch (SQLException e) {
            LOGGER.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }

    @И("получить информацию из таблицы {string}")
    public void getSqlInformation(String table) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            LOGGER.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
            while(resultSet.next()){
                String product = resultSet.getString("PRODUCT");
                String price = resultSet.getString("PRICE");
                LOGGER.info(() -> String.format("%s - %s \n", product, price));
            }
        } catch (SQLException e) {
            LOGGER.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }

    @И("выполнить запрос {string}")
    public void executeSqlQuery(String query) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            LOGGER.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }

    @И("удалить запись {string} в таблице {string}")
    public void deleteByProduct(String data, String table) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            LOGGER.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + table + " WHERE PRODUCT = '" + data + "'");
        } catch (SQLException e) {
            LOGGER.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }

    @И("удалить таблицу {string}")
    public void deleteTable(String table) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            LOGGER.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE " + table);
        } catch (SQLException e) {
            LOGGER.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }
}

