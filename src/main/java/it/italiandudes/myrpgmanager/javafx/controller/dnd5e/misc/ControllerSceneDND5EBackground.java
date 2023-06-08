package it.italiandudes.myrpgmanager.javafx.controller.dnd5e.misc;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.data.misc.Background;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.alert.InformationAlert;
import it.italiandudes.myrpgmanager.javafx.controller.dnd5e.ControllerSceneDND5EList;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public final class ControllerSceneDND5EBackground {

    // Attributes
    private Background background = null;

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private TextArea textAreaDescription;

    // Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        String backgroundName = ControllerSceneDND5EList.getElementName();
        if (backgroundName != null) {
            initExistingBackground(backgroundName);
        }
    }

    // EDT
    @FXML
    private void backToElementList() {
        Client.getStage().setScene(ControllerSceneDND5EList.getListScene());
    }
    @FXML
    private void save() {
        if (textFieldName.getText().replace(" ", "").equals("")) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome al talento.");
            return;
        }
        Service<Void> saveService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            if (background == null) {
                                background = new Background(
                                        null,
                                        textFieldName.getText(),
                                        textAreaDescription.getText()
                                );
                            } else {
                                background = new Background(
                                        background.getBackgroundID(),
                                        textFieldName.getText(),
                                        textAreaDescription.getText()
                                );
                            }

                            background.saveIntoDatabase(null);
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Aggiornamento Dati", "Aggiornamento dei dati effettuato con successo!"));
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Salvataggio", "Si e' verificato un errore durante il salvataggio dei dati");
                                Client.getStage().setScene(ControllerSceneDND5EList.getListScene());
                            });
                        }
                        return null;
                    }
                };
            }
        };

        saveService.start();
    }

    // Methods
    private void initExistingBackground(@NotNull final String backgroundName) {
        Service<Void> talentInitializerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {

                            background = new Background(backgroundName);
                            Platform.runLater(() -> {
                                textFieldName.setText(background.getName());
                                textAreaDescription.setText(background.getDescription());
                            });

                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere l'elemento dal database");
                                Client.getStage().setScene(ControllerSceneDND5EList.getListScene());
                            });
                            throw e;
                        }
                        return null;
                    }
                };
            }
        };

        talentInitializerService.start();
    }
}
