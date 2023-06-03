package it.italiandudes.myrpgmanager.javafx.controller;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.scene.SceneLoading;
import it.italiandudes.myrpgmanager.javafx.utils.RPGRecognizer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class ControllerSceneDBChooser {

    //Graphic Elements
    @FXML private Button buttonDBChooser;
    @FXML private TextField textFieldPath;
    @FXML private Button buttonCreateOrOpenDB;
    @FXML private ListView<String> listViewOptions;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
        listViewOptions.setItems(FXCollections.observableList(MyRPGManager.Defs.SupportedRPGs.SUPPOERTED_RPGS));
        listViewOptions.getSelectionModel().selectFirst();
    }

    // EDT
    @FXML
    private void handleOnDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
    }
    @FXML
    private void handleOnDragDropped(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            String path = event.getDragboard().getFiles().get(0).getAbsolutePath();
            textFieldPath.setText(path);
            event.setDropCompleted(true);
        } else {
            event.setDropCompleted(false);
        }
    }
    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il Database");
        String selectedOption = listViewOptions.getSelectionModel().getSelectedItem();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(selectedOption, "*." + selectedOption));
        fileChooser.setInitialDirectory(new File(MyRPGManager.Defs.JAR_POSITION).getParentFile());
        File fileDB = fileChooser.showOpenDialog(buttonDBChooser.getScene().getWindow());
        if(fileDB!=null) {
            textFieldPath.setText(fileDB.getAbsolutePath());
        }
    }
    @FXML
    private void openDB() {
        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());

        Service<Void> dbOpenerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        File dbPath = new File(textFieldPath.getText());
                        String result = null;

                        try {
                            if (dbPath.exists() && dbPath.isFile() && dbPath.getAbsolutePath().endsWith(listViewOptions.getSelectionModel().getSelectedItem())) {
                                result = DBManager.connectToDB(dbPath);
                            } else {
                                DBManager.createDB(dbPath, listViewOptions.getSelectionModel().getSelectedItem());
                                result = listViewOptions.getSelectionModel().getSelectedItem();
                            }
                        } catch (IOException | SQLException e) {
                            Logger.log(e);
                        }

                        if (result == null) {
                            Platform.runLater(() -> {
                                Client.getStage().setScene(thisScene);
                                new ErrorAlert("ERRORE", "Errore di Connessione al DB", "Si e' verificato un errore nella connessione al database.");
                            });
                            return null;
                        }

                        if (!RPGRecognizer.openRPGTask(result)) {
                            Platform.runLater(() -> {
                                Client.getStage().setScene(thisScene);
                                new ErrorAlert("ERRORE", "Errore di rilevamento dell'estensione", "Si è verificato un errore nell'identificare il tipo di RPG da usare.");
                            });
                        }

                        return null;
                    }
                };
            }
        };

        dbOpenerService.start();
    }

}
