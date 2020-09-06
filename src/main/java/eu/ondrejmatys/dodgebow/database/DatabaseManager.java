package eu.ondrejmatys.dodgebow.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private String host;
    private String port;
    private String db;
    private String user;
    private String password;
    private boolean useSsl;

    private Connection connection;

    public DatabaseManager(String host, String port, String db, String user, String password, boolean useSsl) {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.password = password;
        this.useSsl = useSsl;
        createTables();
    }

    public Connection openConnection() throws SQLException {
        if (connection != null) {
            if (!connection.isClosed()) {
                connection.close();
            }
        }

        return connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s?useSSL=%s", host, port, db, useSsl), user, password);
    }

    private void createTables() {
        try {
            Connection connection = openConnection();

            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `dodgebow_stats` ( `id` INT NOT NULL AUTO_INCREMENT , `uuid` TEXT NOT NULL , `name` VARCHAR(16) NOT NULL , `kills` INT NOT NULL , `wins` INT NOT NULL , `games_played` INT NOT NULL , `deaths` INT NOT NULL , PRIMARY KEY (`id`))");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
