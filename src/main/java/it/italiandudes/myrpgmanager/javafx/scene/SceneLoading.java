package it.italiandudes.myrpgmanager.javafx.scene;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.javafx.JFXDefs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

@SuppressWarnings("unused")
public final class SceneLoading {

    //Scene Generator
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(JFXDefs.Resource.get(JFXDefs.Resource.FXML.FXML_LOADING)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}