package it.italiandudes.myrpgmanager.javafx.scene.dns;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.javafx.JFXDefs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public final class SceneDNSList {

    //Scene Generator
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(MyRPGManager.Defs.Resources.get(JFXDefs.Resource.FXML.DNS.FXML_ITEM_LIST)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}
