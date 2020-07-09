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
    Logger logger = LoggerFactory.getLogger(SqlSteps.class);

    String url;
    String username;
    String password;

    /*Блок получения данных для подключения к БД. Параметры подключения нужно менять в файле database.properties*/
    {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(new File("src/test/resources/database.properties")));
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
            logger.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            String createRequest = "CREATE TABLE " + name + "(\n" +
                    "\tID serial PRIMARY KEY,\n" +
                    "\tPRODUCT VARCHAR (255) NOT NULL,\n" +
                    "\tPRICE integer NOT NULL\n" +
                    ");";
            statement.executeUpdate(createRequest);
            logger.info(() -> "Таблица создана!");
        } catch (SQLException e) {
            logger.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }

    @И("получить информацию из таблицы {string}")
    public void getSqlInformation(String table) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            logger.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
            while(resultSet.next()){
                String product = resultSet.getString("PRODUCT");
                String price = resultSet.getString("PRICE");
                logger.info(() -> String.format("%s - %s \n", product, price));
            }
        } catch (SQLException e) {
            logger.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }

    @И("выполнить запрос {string}")
    public void executeSqlQuery(String query) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            logger.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }

    @И("удалить таблицу {string}")
    public void deleteTable(String table) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            logger.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            statement.executeQuery("DROP TABLE " + table);
        } catch (SQLException e) {
            logger.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }
}

