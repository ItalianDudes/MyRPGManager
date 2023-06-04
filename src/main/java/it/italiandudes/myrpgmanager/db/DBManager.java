package it.italiandudes.myrpgmanager.db;

import it.italiandudes.myrpgmanager.MyRPGManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

@SuppressWarnings("unused")
public final class DBManager {

    // Attributes
    private static Connection dbConnection = null;
    private static final String DB_PREFIX = "jdbc:sqlite:";
    private static final Properties CONNECTION_PROPERTIES = initProperties();

    // Connection Property Initializers
    private static Properties initProperties() {
        Properties properties = new Properties();
        properties.put("allowMultiQueries",true);
        return properties;
    }

    // Generic SQLite Connection Initializer
    private static void setConnection(@NotNull final String DB_ABSOLUTE_PATH) throws SQLException {
        dbConnection = DriverManager.getConnection(DB_PREFIX + DB_ABSOLUTE_PATH, CONNECTION_PROPERTIES);
        setConnectionProperties();
    }
    private static void setConnectionProperties() throws SQLException {
        if (dbConnection == null || dbConnection.isClosed()) return;
        dbConnection.setAutoCommit(true);
    }

    // Methods
    @NotNull
    public static String connectToDB(@NotNull final File DB_PATH) throws IOException, SQLException {
        if (!DB_PATH.exists() || DB_PATH.isDirectory()) throw new IOException("This db doesn't exist");

        String[] pointParts = DB_PATH.getAbsolutePath().split("\\.");
        String RPG_TYPE = pointParts[pointParts.length-1];

        if (!MyRPGManager.Defs.SupportedRPGs.SUPPOERTED_RPGS.contains(RPG_TYPE.toLowerCase())) {
            throw new IOException("This RPG is not supported!");
        }

        setConnection(DB_PATH.getAbsolutePath());
        return RPG_TYPE;
    }
    public static void createDB(@NotNull final File DB_PATH, @NotNull final String RPG_TYPE) throws IOException, SQLException {
        if (DB_PATH.exists() && DB_PATH.isFile()) throw new IOException("This db already exists");

        String DB_ABS_PATH = DB_PATH.getAbsolutePath();

        //noinspection SwitchStatementWithTooFewBranches
        switch (RPG_TYPE) {
            case MyRPGManager.Defs.SupportedRPGs.DND5E.RPG_NAME:
                createDND5EDatabase(DB_ABS_PATH);
                break;

            default:
                throw new IOException("This RPG is not supported!");
        }
    }
    public static void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            }catch (Exception ignored){}
        }
    }
    public static PreparedStatement preparedStatement(@NotNull final String query) throws SQLException {
        if (dbConnection != null) {
            return dbConnection.prepareStatement(query);
        }
        return null;
    }

    // DB Creator
    public static void createDND5EDatabase(@NotNull final String DB_PATH) throws SQLException {
        setConnection(DB_PATH);
        Scanner reader = new Scanner(MyRPGManager.Defs.Resources.getAsStream(MyRPGManager.Defs.Resources.SQL.SQL_DND5E), "UTF-8");
        StringBuilder queryReader = new StringBuilder();

        while (reader.hasNext()) {
            queryReader.append(reader.nextLine());
        }
        reader.close();

        String query = queryReader.toString();
        PreparedStatement ps = dbConnection.prepareStatement(query);
        ps.execute();
        ps.close();
    }

}
