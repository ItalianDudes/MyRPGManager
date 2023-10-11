package it.italiandudes.myrpgmanager.javafx;

import it.italiandudes.myrpgmanager.MyRPGManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;

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
                public static final class Misc {
                    private static final String MISC_DIR = DND5E_DIR + "misc/";
                    public static final String FXML_TALENT = MISC_DIR + "SceneDND5ETalent.fxml";
                    public static final String FXML_LANGUAGE = MISC_DIR + "SceneDND5ELanguage.fxml";
                    public static final String FXML_BACKGROUND = MISC_DIR + "SceneDND5EBackground.fxml";
                }
                public static final class Item {
                    private static final String ITEM_DIR = DND5E_DIR + "item/";
                    public static final String FXML_ITEM = ITEM_DIR + "SceneDND5EItem.fxml";
                    public static final String FXML_ARMOR = ITEM_DIR + "SceneDND5EArmor.fxml";
                    public static final String FXML_WEAPON = ITEM_DIR + "SceneDND5EWeapon.fxml";
                    public static final String FXML_SPELL = ITEM_DIR + "SceneDND5ESpell.fxml";
                    public static final String FXML_EQUIPMENT_PACK = ITEM_DIR + "SceneDND5EEquipmentPack.fxml";
                }
            }
            public static final class DNS {
                private static final String DNS_DIR = FXML_DIR + "dns/";
                public static final String FXML_ITEM_LIST = DNS_DIR + "SceneDNSList.fxml";
            }
        }

        //Image Location
        public static final class Image {
            private static final String IMAGE_DIR = "/image/";
            public static final String IMAGE_LOGO = IMAGE_DIR+"app-logo.png";
        }

    }

}