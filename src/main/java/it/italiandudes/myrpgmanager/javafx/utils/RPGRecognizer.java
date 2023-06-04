package it.italiandudes.myrpgmanager.javafx.utils;

import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.scene.dnd5e.SceneDND5EList;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;

public final class RPGRecognizer {

    // NB: MUST ACCESS FROM A SERVICE, NOT FROM UI THREAD
    public static boolean openRPGTask(@NotNull final String RPG_NAME) {

        //noinspection SwitchStatementWithTooFewBranches
        switch (RPG_NAME) {

            case MyRPGManager.Defs.SupportedRPGs.DND5E.RPG_NAME: // GOTO: DND5E
                Platform.runLater(() -> Client.getStage().setScene(SceneDND5EList.getScene()));
                break;

            default:
                DBManager.closeConnection();
                return false;
        }

        return true;

    }

}
