package skybridge.database;/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

import skybridge.SkyBridge;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 1/6/2017.
 */
public class SkyDB {
    private SkyBridge instance;

    private String host;
    private String name;
    private String user;
    private String pass;
    private String table;
    private Connection connection;

    public SkyDB(SkyBridge instance, String host, String table, String name, String user, String pass) {
        try {
            this.instance = instance;
            this.host = host;
            this.table = table;
            this.name = name;
            this.user = user;
            this.pass = pass;

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + getHost(), getUser(), getPass());


            SETUP_TABLE();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SkyBridge getInstance() {
        return instance;
    }

    /**
     * Core SQL
     **/

    public String getHost() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getTable() {
        return table;
    }

    public Connection getConnection() {
        return connection;
    }

    public void SETUP_TABLE() {
        String query = "CREATE TABLE IF NOT EXISTS " + getTable() + " (id INT NOT NULL AUTO_INCREMENT, account_id INT, perk VARCHAR(60), purchased BIGINT, progress INT, PRIMARY KEY (id));";
        String users = "CREATE TABLE IF NOT EXISTS " + getTable() + "_users (id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(60), points INT(11), coins INT(11), kills INT(11), deaths INT(11), won INT(11), played INT(11), name VARCHAR(16), PRIMARY KEY(id));";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(query);
            executeAsync(statement);
            statement = getConnection().prepareStatement(users);
            executeAsync(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String GET_TABLE() {
        return getTable();
    }

    /**
     * Util
     **/
    public void executeAsync(final PreparedStatement statement) {
        instance.getServer().getScheduler().runTaskAsynchronously(getInstance(),
                new Runnable() {
                    public void run() {
                        try {
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void setStat(String uuid, String name, int value) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE " + GET_TABLE() + "_users SET " + name + "='" + value + "' WHERE uuid='" + uuid + "'");
            executeAsync(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getStats(String uuid) {
        Map<String, Integer> stats = new ConcurrentHashMap<String, Integer>();
        ResultSet set = query("uuid", uuid, true);
        try {
            while (set.next()) {
                stats.put("points", set.getInt("points"));
                stats.put("coins", set.getInt("coins"));
                stats.put("kills", set.getInt("kills"));
                stats.put("deaths", set.getInt("deaths"));
                stats.put("won", set.getInt("won"));
                stats.put("played", set.getInt("played"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    public List<String> getPerks(String uuid) {
        List<String> perks = new ArrayList<String>();
        try {
            ResultSet set = getConnection().prepareStatement("SELECT * FROM " + getTable() + " WHERE account_id=(SELECT id FROM " + getTable() + "_users" + " WHERE uuid='" + uuid + "')").executeQuery();
            while (set.next()) {
                perks.add(set.getString("perk"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return perks;
    }

    public void purchasePerk(String uuid, String perk) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO " + getTable() + " VALUES (NULL, '" + getId(uuid) + "', '" + perk + "', '" + System.currentTimeMillis() + "', 0)");
            executeAsync(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Query for a specific row of data with the modifier, and the value.
     * Example, if you would like to find an entire User row, you would
     * call this method, and on invocation the parameters would be 'uuid', and '{desired uuid you want to find here}'.
     *
     * @param modifier
     * @param desiredTarget
     * @return
     */
    public ResultSet query(String modifier, String desiredTarget, boolean user) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + GET_TABLE() + ((user) ? "_users" : "") + " WHERE " + modifier + "='" + desiredTarget + "';");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                return set;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerUser(String uuid, String name) {
        try {
            ResultSet set = getConnection().prepareStatement("SELECT * FROM " + getTable() + "_users WHERE uuid='" + uuid + "'").executeQuery();
            if (!set.next()) {
                String insert = "INSERT INTO " + getTable() + "_users VALUES (NULL, '" + uuid + "', 0, 0, 0, 0, 0, 0, '" + name + "')";
                PreparedStatement register = getConnection().prepareStatement(insert);
                executeAsync(register);
            } else {
                System.out.println("[SKYBRIDGE] : computing player data for : " + uuid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUUID(String name) {
        String query = "SELECT * FROM " + getTable() + "_users WHERE name='" + name + "';";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                return set.getString("uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName(String uuid) {
        String query = "SELECT * FROM " + getTable() + "_users WHERE uuid='" + uuid + "';";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                return set.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getId(String uuid) {
        String query = "SELECT id FROM " + getTable() + "_users WHERE uuid='" + uuid + "'";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                return set.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
