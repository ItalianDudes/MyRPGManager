package it.italiandudes.myrpgmanager.javafx.controller.dnd5e.misc;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.data.misc.Language;
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
    private Language language = null;

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
                            if (language == null) {
                                language = new Language(
                                        null,
                                        textFieldName.getText(),
                                        textFieldAlphabet.getText(),
                                        textAreaLore.getText()
                                );
                            } else {
                                language = new Language(
                                        language.getLanguageID(),
                                        textFieldName.getText(),
                                        textFieldAlphabet.getText(),
                                        textAreaLore.getText()
                                );
                            }

                            language.saveIntoDatabase(null);
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

                            language = new Language(languageName);
                            Platform.runLater(() -> {
                                textFieldName.setText(language.getName());
                                textFieldAlphabet.setText(language.getAlphabet());
                                textAreaLore.setText(language.getLore());
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
