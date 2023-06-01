package it.italiandudes.myrpgmanager.javafx.controller;

import it.italiandudes.idl.common.FileHandler;
import it.italiandudes.idl.common.SQLiteHandler;
import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.JFXDefs;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;

@SuppressWarnings("unused")
public final class ControllerSceneStartup {

    /*
    //Graphic Elements
    @FXML private BorderPane mainPane;
    @FXML private TextField dbURLTextField;
    @FXML private Button fileChooserButton;
    @FXML private Button startButton;
    @FXML private Button newLocalDBButton;
    @FXML private CheckBox localDBCheckBox;

    //Initialize
    @FXML
    private void initialize(){
        Client.getStage().setResizable(false);
        ImageView fileChooserView = new ImageView(JFXDefs.Resource.get(JFXDefs.Resource.Image.IMAGE_FILE_EXPLORER).toString());
        fileChooserView.setFitWidth(fileChooserButton.getPrefWidth());
        fileChooserView.setFitHeight(fileChooserButton.getHeight());
        fileChooserView.setPreserveRatio(true);
        fileChooserButton.setGraphic(fileChooserView);
    }

    //EDT
    @FXML
    private void handleOnDragOver(DragEvent event){
        if(event.getDragboard().hasFiles()){
            event.acceptTransferModes(TransferMode.COPY);
        }
    }
    @FXML
    private void handleOnDragDropped(DragEvent event){
        if(event.getDragboard().hasFiles()){
            String path = event.getDragboard().getFiles().get(0).getAbsolutePath();
            File fp = new File(path);
            newLocalDBButton.setDisable(!fp.exists() || !fp.isFile());
            dbURLTextField.setText(path);
            event.setDropCompleted(true);
        }else{
            event.setDropCompleted(false);
        }
    }
    @FXML
    private void dbFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il Database");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fermi VolleyBall DB", "*."+JFXDefs.AppInfo.DB_FILE_EXTENSION));
        fileChooser.setInitialDirectory(new File(MyRPGManager.Defs.JAR_POSITION).getParentFile());
        File fileDB = fileChooser.showOpenDialog(fileChooserButton.getScene().getWindow());
        if(fileDB!=null) {
            newLocalDBButton.setDisable(!fileDB.exists() || !fileDB.isFile());
            dbURLTextField.setText(fileDB.getAbsolutePath());
        }
    }
    @FXML
    private void switchDBFilePosition() {
        isDBLocal = localDBCheckBox.isSelected();
        if(isDBLocal) {
            dbURLTextField.setPromptText("Inserisci il percorso completo al database");
            startButton.setText("Apri file esistente");
            fileChooserButton.setDisable(false);
        }else {
            dbURLTextField.setPromptText("Inserisci indirizzo e porta del server");
            startButton.setText("Connettiti al Server");
            fileChooserButton.setDisable(true);
        }
    }
    @FXML
    private void handleStartButton() {
        if(dbURLTextField.getText() == null || dbURLTextField.getText().equals("")) {
            new ErrorAlert("ERRORE", "Errore di Input", "La barra di testo e' vuota");
        }else {
            if (isDBLocal) openLocalDB();
            else connectToServer();
        }
    }
    private void connectToServer() {} //TODO: connectToServer()
    private void openLocalDB() {
        Scene thisScene = dbURLTextField.getScene();
        Client.getStage().setScene(SceneLoading.getScene());
        Service<Void> attemptDBConnectionThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String dbPath = dbURLTextField.getText();
                        assert dbPath!=null && dbPath.equals("");
                        File fileChecker = new File(dbPath);
                        if(!fileChecker.exists() || !fileChecker.isFile()){
                            Platform.runLater(() -> {
                                Client.getStage().setScene(thisScene);
                                new ErrorAlert("ERRORE", "Errore inserimento DB", "Il percorso inserito non esiste oppure non e' un file! Inserire un percorso a un file valido");
                            });
                            return null;
                        }
                        if(!FileHandler.getFileExtension(dbPath).equals("db3")){
                            Platform.runLater(() -> {
                                Client.getStage().setScene(thisScene);
                                new ErrorAlert("ERRORE", "Errore inserimento DB", "Il file inserito non e' di formato "+JFXDefs.AppInfo.DB_FILE_EXTENSION +"! Inserire un percorso a un file valido");
                            });
                            return null;
                        }
                        Connection dbConnection = SQLiteHandler.openConnection(dbURLTextField.getText());

                        if(dbConnection==null){
                            Platform.runLater(() -> {
                                Client.getStage().setScene(thisScene);
                                new ErrorAlert("ERRORE", "Errore connessione DB", "Si e' verificato un errore durante la connessione al database");
                            });
                            return null;
                        }

                        if(!FermiVolleyBall.setDbConnection(dbConnection)){
                            throw new RuntimeException("There is already an open connection with a database");
                        }
                        Platform.runLater(() -> Client.getStage().setScene(SceneMenu.getScene()));
                        return null;
                    }
                };
            }
        };
        attemptDBConnectionThread.start();
    }
    @FXML
    private void createNewLocalDB() {} //TODO: createNewLocalDB()
*/
}