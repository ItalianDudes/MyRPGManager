package it.italiandudes.myrpgmanager.javafx.scene;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.javafx.JFXDefs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public final class SceneCreateOrChooseDB {

    //Scene Generator
    // NB: This is the Application Entry Point
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(MyRPGManager.Defs.Resources.get(JFXDefs.Resource.FXML.FXML_CREATE_OR_CHOOSE_DB)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}
