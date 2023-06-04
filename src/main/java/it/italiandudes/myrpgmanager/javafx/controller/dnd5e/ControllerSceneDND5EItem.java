package it.italiandudes.myrpgmanager.javafx.controller.dnd5e;

import it.italiandudes.myrpgmanager.javafx.Client;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

@SuppressWarnings("unused")
public final class ControllerSceneDND5EItem {

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private TextField textFieldWeight;
    @FXML private ComboBox<String> comboBoxRarity;
    @FXML private TextField textFieldMR;
    @FXML private TextField textFieldMA;
    @FXML private TextField textFieldME;
    @FXML private TextField textFieldMO;
    @FXML private TextField textFieldMP;
    @FXML private TextArea textAreaDescription;
    @FXML private ImageView imageViewItem;

    // Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        // TODO: initialize()
    }

    // EDT
    @FXML
    private void save() {
        // TODO: save()
    }
}
