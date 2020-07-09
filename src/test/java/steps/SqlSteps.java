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
        //todo
    }

    @И("получить информацию из таблицы {string}")
    public void getSqlInformation(String table) {
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            logger.info(() -> "Подключение к базе данных прошло успешно!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String price = resultSet.getString("duration");
                logger.info(() -> String.format("%s - %s \n", name, price));
            }
        } catch (SQLException e) {
            logger.error(() -> "Ошибка выполнения запроса\n");
            e.printStackTrace();
        }
    }
}
