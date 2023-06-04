package it.italiandudes.myrpgmanager.javafx;

import it.italiandudes.myrpgmanager.MyRPGManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;

@SuppressWarnings("unused")
public final class JFXDefs {

    //App Info
    public static final class AppInfo {
        public static final String NAME = "MyRPGManager";
        public static final Image LOGO = new Image(MyRPGManager.Defs.Resources.get(Resource.Image.IMAGE_LOGO).toString());
    }

    // System Info
    public static final class SystemGraphicInfo {
        public static final Rectangle2D SCREEN_RESOLUTION = Screen.getPrimary().getBounds();
        public static final double SCREEN_WIDTH = SCREEN_RESOLUTION.getWidth();
        public static final double SCREEN_HEIGHT = SCREEN_RESOLUTION.getHeight();
    }

    //CSS Functions
    public static final class CSS {}

    //Resource Locations
    public static final class Resource {

        //FXML Location
        public static final class FXML {
            private static final String FXML_DIR = "/fxml/";
            public static final String FXML_LOADING = FXML_DIR + "SceneLoading.fxml";
            public static final String FXML_CREATE_OR_CHOOSE_DB = FXML_DIR + "SceneDBChooser.fxml";
            public static final class DND5E {
                private static final String DND5E_DIR = FXML_DIR + "dnd5e/";
                public static final String FXML_ITEM_LIST = DND5E_DIR + "SceneDND5EList.fxml";
            }
        }

        //GIF Location
        public static final class GIF {
            private static final String GIF_DIR = "/gif/";
            public static final String GIF_LOADING = GIF_DIR+"loading.gif";
        }

        //Image Location
        public static final class Image {
            private static final String IMAGE_DIR = "/image/";
            public static final String IMAGE_LOGO = IMAGE_DIR+"app-logo.png";
            public static final String IMAGE_FILE_EXPLORER = IMAGE_DIR+"file-explorer.png";
        }

    }

}