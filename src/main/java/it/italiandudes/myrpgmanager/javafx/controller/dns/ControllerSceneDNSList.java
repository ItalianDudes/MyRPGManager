package it.italiandudes.myrpgmanager.javafx.controller.dns;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.data.common.Rarity;
import it.italiandudes.myrpgmanager.data.dns.DNSCategory;
import it.italiandudes.myrpgmanager.data.dns.DNSElementPreview;
import it.italiandudes.myrpgmanager.data.dns.DNSEquipmentType;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.scene.SceneCreateOrChooseDB;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class ControllerSceneDNSList {

    // Attributes
    @Nullable static String elementName = null;

    // Methods
    @Nullable
    public static String getElementName() {
        return elementName;
    }

    // Graphics Elements
    @FXML private TextField textFieldSearchBar;
    @FXML private ComboBox<DNSCategory> comboBoxCategory;
    @FXML private ComboBox<DNSEquipmentType> comboBoxEquipmentType;
    @FXML private TableView<DNSElementPreview> tableViewElements;
    @FXML private TableColumn<DNSElementPreview, Integer> tableColumnID;
    @FXML private TableColumn<DNSElementPreview, String> tableColumnName;
    @FXML private TableColumn<DNSElementPreview, Rarity> tableColumnRarity;
    @FXML private TableColumn<DNSElementPreview, Double> tableColumnWeight;
    @FXML private TableColumn<DNSElementPreview, Integer> tableColumnCostCopper;

    // Initialize
    @FXML
    private void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnRarity.setCellValueFactory(new PropertyValueFactory<>("rarity"));
        tableColumnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        tableColumnCostCopper.setCellValueFactory(new PropertyValueFactory<>("costCopper"));
        comboBoxCategory.setItems(FXCollections.observableList(DNSCategory.categories));
        comboBoxEquipmentType.setItems(FXCollections.observableList(DNSEquipmentType.types));
        search();
    }

    // EDT
    @FXML @SuppressWarnings("DuplicatedCode")
    private void search() {
        DNSCategory selectedCategory = comboBoxCategory.getSelectionModel().getSelectedItem();
        comboBoxEquipmentType.setDisable(selectedCategory == null || !selectedCategory.equals(DNSCategory.EQUIPMENT));
        DNSEquipmentType equipmentType = comboBoxEquipmentType.getSelectionModel().getSelectedItem();
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            String query;
                            PreparedStatement ps;
                            if (selectedCategory != null) {
                                if (selectedCategory.equals(DNSCategory.EQUIPMENT) && equipmentType != null) {
                                    query = "SELECT i.id AS id, i.name AS name, i.rarity AS rarity, i.weight AS weight, i.cost_copper AS cost_copper, i.quantity AS quantity FROM items AS i JOIN equipments AS e ON i.id = e.item_id WHERE i.name LIKE '%" + textFieldSearchBar.getText() + "%' AND i.category=? AND e.type=?;";
                                    ps = DBManager.preparedStatement(query);
                                    if (ps == null) {
                                        Platform.runLater(() -> {
                                            new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                            Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                        });
                                        return null;
                                    }
                                    ps.setInt(1, selectedCategory.getDatabaseValue());
                                    ps.setInt(2, equipmentType.getDatabaseValue());
                                } else {
                                    query = "SELECT id, name, rarity, weight, cost_copper, quantity FROM items WHERE name LIKE '%" + textFieldSearchBar.getText() + "%' AND category=?;";
                                    ps = DBManager.preparedStatement(query);
                                    if (ps == null) {
                                        Platform.runLater(() -> {
                                            new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                            Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                        });
                                        return null;
                                    }
                                    ps.setInt(1, selectedCategory.getDatabaseValue());
                                }
                            } else {
                                query = "SELECT id, name, rarity, weight, cost_copper FROM items WHERE name LIKE '%"+textFieldSearchBar.getText()+"%';";
                                ps = DBManager.preparedStatement(query);
                                if (ps == null) {
                                    Platform.runLater(() -> {
                                        new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                        Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                    });
                                    return null;
                                }
                            }

                            ResultSet result = ps.executeQuery();

                            ArrayList<DNSElementPreview> resultList = new ArrayList<>();

                            while (result.next()) {
                                resultList.add(
                                        new DNSElementPreview(
                                                result.getInt("id"),
                                                result.getString("name"),
                                                DNSCategory.values()[result.getInt("category")],
                                                Rarity.values()[result.getInt("rarity")],
                                                result.getDouble("weight"),
                                                result.getInt("cost_copper")
                                        ));
                            }

                            ps.close();
                            Platform.runLater(() -> tableViewElements.setItems(FXCollections.observableList(resultList)));
                        } catch (Exception e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "ERRORE DI CONNESSIONE", "Si e' verificato un errore durante la comunicazione con il database"));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    @FXML
    private void resetSearchBarAndCategory() {
        textFieldSearchBar.setText(null);
        comboBoxCategory.getSelectionModel().clearSelection();
        comboBoxEquipmentType.getSelectionModel().clearSelection();
        comboBoxEquipmentType.setDisable(true);
        search();
    }
    @FXML
    private void delete() {
        DNSElementPreview elementPreview = tableViewElements.getSelectionModel().getSelectedItem();
        if (elementPreview == null) return;
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            PreparedStatement ps = DBManager.preparedStatement("DELETE FROM items WHERE id=?;");
                            if (ps == null) throw new SQLException("The database connection doesn't exist");
                            ps.setInt(1, elementPreview.getId());
                            ps.executeUpdate();
                            ps.close();
                            Platform.runLater(ControllerSceneDNSList.this::search);
                        } catch (SQLException e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Rimozione", "Si e' verificato un errore durante la rimozione dell'elemento."));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
    public Scene selectEquipmentScene(@Nullable final DNSElementPreview element) {
        DNSEquipmentType equipmentType;
        if (element != null) {
            try {
                equipmentType = new DNSEquipment(element.getName()).getType();
            } catch (SQLException e) {
                new ErrorAlert("ERRORE", "ERRORE COL DATABASE", "Si e' verificato un errore durante l'interrogazione del database.");
                return null;
            }
        } else {
            equipmentType = comboBoxEquipmentType.getSelectionModel().getSelectedItem();
            if (equipmentType == null) {
                new ErrorAlert("ERRORE", "ERRORE DI INSERIMENTO", "Per aggiungere dell'equipaggiamento devi prima selezionare il tipo.");
                return null;
            }
        }
        switch (equipmentType) {
            case ARMOR:
                return SceneInventoryArmor.getScene();
            case WEAPON:
                return SceneInventoryWeapon.getScene();
            case ADDON:
                return SceneInventoryAddon.getScene();
            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria equipaggiamento valida.");
                return null;
        }
    }
    @FXML
    private void edit() {
        DNSElementPreview elementPreview = tableViewElements.getSelectionModel().getSelectedItem();
        if (elementPreview == null) return;
        elementName = elementPreview.getName();
        Scene scene;
        switch (elementPreview.getCategory()) {
            case ITEM:
                scene = SceneDNSItem.getScene();
                break;
            case EQUIPMENT:
                scene = selectEquipmentScene(elementPreview);
                if (scene == null) return;
                break;
            case SPELL:
                scene = SceneDNSSpell.getScene();
                break;
            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria valida o non e' stata ancora implementata nell'applicazione.");
                return;
        }
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        elementName = null;
        search();
    }
    @FXML
    private void add() {
        elementName = null;
        Scene scene;
        if (comboBoxCategory.getSelectionModel().isEmpty()) {
            new ErrorAlert("ERRORE", "ERRORE DI PROCEDURA", "Per aggiungere un elemento e' necessario prima selezionare una categoria.");
            return;
        }
        DNSCategory category = comboBoxCategory.getSelectionModel().getSelectedItem();
        switch (category) {
            case ITEM:
                scene = SceneDNSItem.getScene();
                break;

            case EQUIPMENT:
                scene = selectEquipmentScene(null);
                if (scene == null) return;
                break;

            case SPELL:
                scene = SceneDNSSpell.getScene();
                break;

            default: // Invalid
                new ErrorAlert("ERRORE", "ERRORE NEL DATABASE", "L'elemento selezionato non possiede una categoria valida o non e' stata ancora implementata nell'applicazione.");
                return;
        }
        Stage popupStage = Client.initPopupStage(scene);
        popupStage.showAndWait();
        search();
    }
    @FXML
    private void tableDoubleClickEdit(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) edit();
    }
    @FXML
    private void tableDetectEnterOnRow(@NotNull final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && tableViewElements.getSelectionModel().getSelectedItem() != null) {
            edit();
        }
    }
}
