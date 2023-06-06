package it.italiandudes.myrpgmanager.interfaces;

import java.sql.SQLException;

@SuppressWarnings("unused")
public interface ISavable {
    void saveIntoDatabase() throws SQLException;
}
