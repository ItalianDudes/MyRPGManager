package it.italiandudes.myrpgmanager.javafx.utils;

import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.scene.dnd5e.SceneDND5EMainMenu;
import org.jetbrains.annotations.NotNull;

public final class RPGRecognizer {

    // NB: MUST ACCESS FROM A SERVICE, NOT FROM UI THREAD
    public static boolean openRPGTask(@NotNull final String RPG_NAME) {

        //noinspection SwitchStatementWithTooFewBranches
        switch (RPG_NAME) {

            case MyRPGManager.Defs.SupportedRPGs.DND5E: // GOTO: DND5E
                Client.getStage().setScene(SceneDND5EMainMenu.getScene());
                break;

            default:
                DBManager.closeConnection();
                return false;
        }

        return true;

    }

}
