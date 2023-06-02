package it.italiandudes.myrpgmanager;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.javafx.Client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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

        // Supported Games
        public static final class SupportedRPGs {
            public static final String DND5E = "dnd5e";
            public static final ArrayList<String> SUPPOERTED_RPGS = (ArrayList<String>) Arrays.asList(
                new String[]{
                        DND5E
                }
            );
        }

    }

}
