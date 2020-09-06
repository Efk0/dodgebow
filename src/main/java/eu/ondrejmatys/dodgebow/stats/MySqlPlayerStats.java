package eu.ondrejmatys.dodgebow.stats;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;
import eu.ondrejmatys.dodgebow.config.configs.MysqlConfig;
import eu.ondrejmatys.dodgebow.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlPlayerStats implements PlayerStats {

    private DatabaseManager databaseManager;

    public MySqlPlayerStats()  {
        SimpleConfig mysqlConfig = DodgeBow.getInstance().mysqlConfig;
        databaseManager = new DatabaseManager(
                mysqlConfig.getString("host"),
                mysqlConfig.getString("port"),
                mysqlConfig.getString("db"),
                mysqlConfig.getString("user"),
                mysqlConfig.getString("pass"),
                mysqlConfig.getBoolean("useSsl")
        );
    }

    @Override
    public Object getValue(String playerName, String valueName) {
        try {
            Connection connection = databaseManager.openConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM dodgebow_stats WHERE name=?");
            statement.setString(1, playerName);

            ResultSet result = statement.executeQuery();

            Object valueToReturn;
            if (result.first()) {
                valueToReturn = result.getObject(valueName);
            } else {
                valueToReturn = 0;
            }
            connection.close();

            return valueToReturn;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public void setValue(String playerName, String valueName, String value) {
        try {
            Connection connection = databaseManager.openConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE dodgebow_stats SET ?=? WHERE name=?");
            statement.setString(1, valueName);
            statement.setString(2, value);
            statement.setString(3, playerName);

            statement.executeUpdate();

            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
