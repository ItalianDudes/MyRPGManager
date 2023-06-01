package it.italiandudes.myrpgmanager;

import it.italiandudes.idl.common.Logger;

import java.io.File;

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

        // Add Logger Close Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
    }

    // Defs
    public static class Defs {

        // Jar App Position
        public static final String JAR_POSITION = new File(MyRPGManager.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
    }

}
