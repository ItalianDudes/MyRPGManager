package it.italiandudes.myrpgmanager;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.javafx.Client;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public final class MyRPGManager {

    // Main Method
    public static void main(String[] args) {

        // Init the app logger
        try {
            Logger.init();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Shutdown Hooks Configurator
        configureShutdownHooks();

        // Start UI
        Client.start(args);
    }

    // Shutdown Hooks Configurator
    private static void configureShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Client.getStage().close()));
    }

    // Defs
    public static class Defs {

        // Jar App Position
        public static final String JAR_POSITION = new File(MyRPGManager.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();

        // Resources Location
        public static final class Resources {
            //Resource Getter
            public static URL get(@NotNull final String resourceConst){
                return Objects.requireNonNull(MyRPGManager.class.getResource(resourceConst));
            }
            public static InputStream getAsStream(@NotNull final String resourceConst){
                return Objects.requireNonNull(MyRPGManager.class.getResourceAsStream(resourceConst));
            }

            // SQL Location
            public static final class SQL {
                private static final String SQL_DIR = "/sql/";
                public static final String SQL_DND5E = SQL_DIR + "dnd5e.sql";
            }
        }

        // Supported Games
        public static final class SupportedRPGs {

            // DND5E
            public static final class DND5E {
                public static final String RPG_NAME = "dnd5e";
                public static final String[] ITEMS = {"Oggetti","items"};
                public static final String[] CLASSES = {"Classi","classes"};
                public static final String[] RACES = {"Razze","races"};
                public static final String[] TALENTS = {"Talenti","talents"};
                public static final String[] BACKGROUNDS = {"Background","backgrounds"};
                public static final String[] LANGUAGES = {"Lingue", "languages"};
                public static final ArrayList<String> ELEMENTS = new ArrayList<>(
                    Arrays.asList(
                            ITEMS[0],
                            CLASSES[0],
                            RACES[0],
                            TALENTS[0],
                            BACKGROUNDS[0],
                            LANGUAGES[0]
                    )
                );
            }

            // Supported RPGs List
            @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
            public static final ArrayList<String> SUPPOERTED_RPGS = new ArrayList<>(
                    Arrays.asList(
                            DND5E.RPG_NAME
                    )
            );
        }

    }

}
