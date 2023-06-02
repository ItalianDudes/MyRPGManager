package it.italiandudes.myrpgmanager.javafx;

import it.italiandudes.myrpgmanager.javafx.scene.SceneCreateOrChooseDB;
import javafx.application.Application;
import javafx.stage.Stage;
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
    }

    //Start Methods
    public static int start(String[] args){
        launch(args);
        return 0;
    }

    //Methods
    @NotNull
    public static Stage getStage(){
        return stage;
    }

}