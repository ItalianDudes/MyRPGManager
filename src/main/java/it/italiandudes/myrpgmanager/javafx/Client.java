package it.italiandudes.myrpgmanager.javafx;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.javafx.scene.SceneCreateOrChooseDB;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class Client extends Application {

    //Attributes
    private static Clipboard SYSTEM_CLIPBOARD = null;
    private static Stage stage;
    private static Image DEFAULT_IMAGE = null;

    @Override
    public void start(Stage stage) {
        SYSTEM_CLIPBOARD = Clipboard.getSystemClipboard();
        Client.stage = stage;
        stage.setTitle(JFXDefs.AppInfo.NAME);
        stage.getIcons().add(JFXDefs.AppInfo.LOGO);
        stage.setScene(SceneCreateOrChooseDB.getScene());
        stage.show();
        stage.setX((JFXDefs.SystemGraphicInfo.SCREEN_WIDTH - stage.getWidth()) / 2);
        stage.setY((JFXDefs.SystemGraphicInfo.SCREEN_HEIGHT - stage.getHeight()) / 2);
        stage.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            stage.hide();
            Logger.log("JavaFX Window Close Event fired, exiting Java process...");
            System.exit(0);
        });
        DEFAULT_IMAGE = JFXDefs.AppInfo.LOGO;

        // Notice into the logs that the application started Successfully
        Logger.log("Application started successfully!");
    }

    //Start Methods
    public static void start(String[] args){
        launch(args);
    }

    //Methods
    @NotNull
    public static Clipboard getSystemClipboard() {
        return SYSTEM_CLIPBOARD;
    }
    @NotNull
    public static Stage getStage(){
        return stage;
    }
    @NotNull
    public static Stage initPopupStage(Scene scene) {
        Stage popupStage = new Stage();
        popupStage.getIcons().add(JFXDefs.AppInfo.LOGO);
        popupStage.setTitle(JFXDefs.AppInfo.NAME);
        popupStage.initOwner(getStage());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setScene(scene);
        return popupStage;
    }
    @NotNull
    public static Image getDefaultImage() {
        return DEFAULT_IMAGE;
    }
}
