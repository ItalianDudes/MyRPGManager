package it.italiandudes.myrpgmanager.db;

import it.italiandudes.myrpgmanager.MyRPGManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

@SuppressWarnings({"unused", "SqlSourceToSinkFlow", "SqlDialectInspection", "SqlNoDataSourceInspection"})
public final class DBManager {

    // Attributes
    private static Connection dbConnection = null;
    private static final String DB_PREFIX = "jdbc:sqlite:";

    // Generic SQLite Connection Initializer
    private static void setConnection(@NotNull final String DB_ABSOLUTE_PATH) throws SQLException {
        String url = DB_PREFIX+DB_ABSOLUTE_PATH+"?allowMultiQueries=true";
        dbConnection = DriverManager.getConnection(DB_PREFIX + DB_ABSOLUTE_PATH);
        setConnectionProperties();
    }
    private static void setConnectionProperties() throws SQLException {
        if (dbConnection == null || dbConnection.isClosed()) return;
        dbConnection.setAutoCommit(true);
        Statement st = dbConnection.createStatement();
        st.execute("PRAGMA foreign_keys = ON;");
        st.close();
    }

    // Methods
    @NotNull
    public static String connectToDB(@NotNull final File DB_PATH) throws IOException, SQLException {
        if (!DB_PATH.exists() || DB_PATH.isDirectory()) throw new IOException("This db doesn't exist");

        String[] pointParts = DB_PATH.getAbsolutePath().split("\\.");
        String RPG_TYPE = pointParts[pointParts.length-1];

        if (!MyRPGManager.Defs.SupportedRPGs.SUPPORTED_RPGS.contains(RPG_TYPE.toLowerCase())) {
            throw new IOException("This RPG is not supported!");
        }

        setConnection(DB_PATH.getAbsolutePath());
        return RPG_TYPE;
    }
    public static void createDB(@NotNull final File DB_PATH, @NotNull final String RPG_TYPE) throws IOException, SQLException {
        if (DB_PATH.exists() && DB_PATH.isFile()) throw new IOException("This db already exists");

        String DB_ABS_PATH = DB_PATH.getAbsolutePath();

        switch (RPG_TYPE) {
            case MyRPGManager.Defs.SupportedRPGs.DND5E.RPG_NAME:
                createDatabase(DB_ABS_PATH, MyRPGManager.Defs.Resources.SQL.SQL_DND5E);
                break;

            case MyRPGManager.Defs.SupportedRPGs.DNS.RPG_NAME:
                createDatabase(DB_ABS_PATH, MyRPGManager.Defs.Resources.SQL.SQL_DNS);
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
    public static void commit() throws SQLException {
        if (dbConnection != null) dbConnection.commit();
    }

    // DB Creator
    public static void createDatabase(@NotNull final String DB_PATH, @NotNull final String CREATION_QUERY_FILE_PATH) throws SQLException {
        setConnection(DB_PATH);
        Scanner reader = new Scanner(MyRPGManager.Defs.Resources.getAsStream(CREATION_QUERY_FILE_PATH), "UTF-8");
        StringBuilder queryReader = new StringBuilder();
        String query;
        String buffer;

        while (reader.hasNext()) {
            buffer = reader.nextLine();
            queryReader.append(buffer);
            if (buffer.endsWith(";")) {
                query = queryReader.toString();
                PreparedStatement ps = dbConnection.prepareStatement(query);
                ps.execute();
                ps.close();
                queryReader = new StringBuilder();
            } else {
                queryReader.append('\n');
            }
        }
        reader.close();
    }

}
