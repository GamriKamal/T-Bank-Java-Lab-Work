package irmag.io.TbankTranslator.PostgreSQL;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Objects;

@Component
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final Object lock = new Object();
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static volatile Connection connection;
    private static Environment env;
    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String DB_NAME;
    private static String SERVER_URL;

    public DatabaseConnection(Environment env) {
        DatabaseConnection.env = env;
        initConstants();
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (lock) {
                if (connection == null) {
                    String url = Objects.requireNonNull(env.getProperty("db.url"));
                    String username = Objects.requireNonNull(env.getProperty("db.username"));
                    String password = Objects.requireNonNull(env.getProperty("db.password"));
                    connection = DriverManager.getConnection(url, username, password);
                }
            }
        }
        return connection;
    }

    private static void initConstants() {
        DB_URL = Objects.requireNonNull(env.getProperty("db.url"));
        DB_USERNAME = Objects.requireNonNull(env.getProperty("db.username"));
        DB_PASSWORD = Objects.requireNonNull(env.getProperty("db.password"));
        DB_NAME = Objects.requireNonNull(env.getProperty("db.name"));
        SERVER_URL = DB_URL.substring(0, DB_URL.lastIndexOf('/') + 1);
    }

    @PostConstruct
    public void createDatabase() {
        try (Connection serverConnection = DriverManager.getConnection(SERVER_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = serverConnection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + DB_NAME + "'");
            if (!resultSet.next()) {
                statement.executeUpdate("CREATE DATABASE " + DB_NAME);
                logger.info(GREEN + "Database " + DB_NAME + " created successfully!" + RESET);
            } else {
                logger.info(YELLOW + "Database " + DB_NAME + " already exists." + RESET);
            }

        } catch (SQLException e) {
            if ("42P04".equals(e.getSQLState())) {
                logger.info(YELLOW + "Database " + DB_NAME + " already exists." + RESET);
            } else {
                logger.error(RED + "Error checking/creating database: " + e + RESET);
            }
        }

        createTable();
    }

    private void createTable() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = connection.getMetaData().getTables(null, null, "t_bank_translations", null);
            if (!resultSet.next()) {
                String createTableSQL = new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("schema.sql")).toURI())));
                statement.executeUpdate(createTableSQL);
                logger.info(GREEN + "Table t_bank_translations created successfully!" + RESET);
            } else {
                logger.info(YELLOW + "Table t_bank_translations already exists." + RESET);
            }

        } catch (SQLException | IOException | URISyntaxException e) {
            logger.error(RED + "Error checking/creating table: " + e + RESET);
        }
    }
}
