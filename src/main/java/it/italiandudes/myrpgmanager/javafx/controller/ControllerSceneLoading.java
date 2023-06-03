package it.italiandudes.myrpgmanager.javafx.controller;

import it.italiandudes.myrpgmanager.javafx.Client;
import javafx.fxml.FXML;

@SuppressWarnings("unused")
public final class ControllerSceneLoading {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }
}