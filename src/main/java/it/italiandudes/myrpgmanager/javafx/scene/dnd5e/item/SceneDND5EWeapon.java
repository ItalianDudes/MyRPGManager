package it.italiandudes.myrpgmanager.javafx.scene.dnd5e.item;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.javafx.JFXDefs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

@SuppressWarnings("unused")
public final class SceneDND5EWeapon {

    //Scene Generator
    public static Scene getScene() {
        try {
            return new Scene(FXMLLoader.load(MyRPGManager.Defs.Resources.get(JFXDefs.Resource.FXML.DND5E.Item.FXML_WEAPON)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }
}
