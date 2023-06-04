package it.italiandudes.myrpgmanager.javafx;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.javafx.scene.SceneCreateOrChooseDB;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class Client extends Application {

    //Attributes
    private static Stage stage;

    @Override
    public void start(Stage stage) {
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
    }

    //Start Methods
    public static void start(String[] args){
        launch(args);
    }

    //Methods
    @NotNull
    public static Stage getStage(){
        return stage;
    }

}
