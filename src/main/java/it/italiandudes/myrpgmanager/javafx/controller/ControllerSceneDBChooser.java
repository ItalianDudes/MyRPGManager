package it.italiandudes.myrpgmanager.javafx.controller;

import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;

import java.io.File;

@SuppressWarnings("unused")
public final class ControllerSceneDBChooser {

    //Graphic Elements
    @FXML private Button buttonDBChooser;
    @FXML private TextField textFieldPath;
    @FXML private Button buttonCreateOrOpenDB;
    @FXML private ListView<String> listViewOptions;

    //Initialize
    @FXML
    private void initialize(){
        Client.getStage().setResizable(false);
        listViewOptions.setItems(FXCollections.observableList(MyRPGManager.Defs.SupportedRPGs.SUPPOERTED_RPGS));
        listViewOptions.getSelectionModel().selectFirst();
    }

    // EDT
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
            textFieldPath.setText(path);
            event.setDropCompleted(true);
        }else{
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
    private void gotoDB() {

    }

}
