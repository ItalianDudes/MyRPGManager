package it.italiandudes.myrpgmanager.javafx.controller.dnd5e.misc;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.data.dnd5e.misc.DND5ELanguage;
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
public final class ControllerSceneDND5ELanguage {

    // Attributes
    private DND5ELanguage DND5ELanguage = null;

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private TextField textFieldAlphabet;
    @FXML private TextArea textAreaLore;

    // Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        String languageName = ControllerSceneDND5EList.getElementName();
        if (languageName != null) {
            initExistingLanguage(languageName);
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
                            if (DND5ELanguage == null) {
                                DND5ELanguage = new DND5ELanguage(
                                        null,
                                        textFieldName.getText(),
                                        textFieldAlphabet.getText(),
                                        textAreaLore.getText()
                                );
                            } else {
                                DND5ELanguage = new DND5ELanguage(
                                        DND5ELanguage.getLanguageID(),
                                        textFieldName.getText(),
                                        textFieldAlphabet.getText(),
                                        textAreaLore.getText()
                                );
                            }

                            DND5ELanguage.saveIntoDatabase(null);
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
    private void initExistingLanguage(@NotNull final String languageName) {
        Service<Void> talentInitializerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {

                            DND5ELanguage = new DND5ELanguage(languageName);
                            Platform.runLater(() -> {
                                textFieldName.setText(DND5ELanguage.getName());
                                textFieldAlphabet.setText(DND5ELanguage.getAlphabet());
                                textAreaLore.setText(DND5ELanguage.getLore());
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
