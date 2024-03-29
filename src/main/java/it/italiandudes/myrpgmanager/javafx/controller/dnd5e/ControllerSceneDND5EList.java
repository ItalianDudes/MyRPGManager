package it.italiandudes.myrpgmanager.javafx.controller.dnd5e;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager.Defs.SupportedRPGs.DND5E;
import it.italiandudes.myrpgmanager.data.dnd5e.DND5EElementPreview;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.scene.SceneCreateOrChooseDB;
import it.italiandudes.myrpgmanager.javafx.scene.dnd5e.item.*;
import it.italiandudes.myrpgmanager.javafx.scene.dnd5e.misc.SceneDND5EBackground;
import it.italiandudes.myrpgmanager.javafx.scene.dnd5e.misc.SceneDND5ELanguage;
import it.italiandudes.myrpgmanager.javafx.scene.dnd5e.misc.SceneDND5ETalent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@SuppressWarnings("unused")
public final class ControllerSceneDND5EList {

    // Attributes
    private static Scene thisScene = null;
    private static String elementName = null;
    private static boolean sortDesc = false;

    //Graphic Elements
    @FXML private ComboBox<String> comboBoxCategory;
    @FXML private ComboBox<String> comboBoxSorter;
    @FXML private ComboBox<String> comboBoxFilter;
    @FXML private TextField textFieldSearchBar;
    @FXML private CheckBox checkBoxSortDesc;
    @FXML private ListView<DND5EElementPreview> listViewOptions;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        comboBoxCategory.setItems(FXCollections.observableList(DND5E.ELEMENTS));
        comboBoxSorter.setItems(FXCollections.observableList(DND5E.SORTERS));
        comboBoxSorter.setVisible(false);
        comboBoxSorter.getSelectionModel().selectFirst();
        comboBoxFilter.setItems(FXCollections.observableList(DND5E.ITEM_FILTERS));
        comboBoxFilter.getSelectionModel().selectFirst();
        comboBoxFilter.setVisible(false);
        listViewOptions.setCellFactory(lv -> new ListCell<DND5EElementPreview>() {
            @Override
            protected void updateItem(DND5EElementPreview DND5EElementPreview, boolean empty) {
                super.updateItem(DND5EElementPreview, empty);
                if (empty) {
                    setText(null);
                    setStyle("-fx-font-weight: bold;");
                } else {
                    if (comboBoxCategory.getSelectionModel().getSelectedItem().equals(DND5E.ITEMS[0])) {
                        setText(DND5EElementPreview.getName());
                        assert DND5EElementPreview.getRarity() != null;
                        setStyle("-fx-background-color: "+ DND5EElementPreview.getRarityColor()+";-fx-font-weight: bold;");
                    } else {
                        setText(DND5EElementPreview.getName());
                    }
                }
            }
        });
    }

    // EDT
    @FXML
    private void setSortDesc() {
        sortDesc = checkBoxSortDesc.isSelected();
        search();
    }
    @FXML
    private void applyFilter() {
        String category = comboBoxCategory.getSelectionModel().getSelectedItem();
        if (category == null) return;
        if (!category.equals(DND5E.ITEMS[0])) return;
        search();
    }
    @FXML
    private void applySorter() {
        String category = comboBoxCategory.getSelectionModel().getSelectedItem();
        if (category == null) return;
        if (!category.equals(DND5E.ITEMS[0])) return;
        search();
    }
    @FXML
    private void displaySelected() {
        String category = comboBoxCategory.getSelectionModel().getSelectedItem();
        if (category == null) return;
        comboBoxSorter.setVisible(category.equals(DND5E.ITEMS[0]));
        comboBoxFilter.setVisible(category.equals(DND5E.ITEMS[0]));
        Service<Void> displaySelectedService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            String table = getTableNameByCategory(category);
                            String query;
                            if (category.equals(DND5E.ITEMS[0])) {
                                int filterIndex = comboBoxFilter.getSelectionModel().getSelectedIndex()-1;
                                String filterField;
                                String filter = comboBoxSorter.getSelectionModel().getSelectedItem();
                                if (filter == null || filter.equals(DND5E.SORTER_NAME[0])) {
                                    filterField = DND5E.SORTER_NAME[1];
                                } else if (filter.equals(DND5E.SORTER_RARITY[0])) {
                                    filterField = DND5E.SORTER_RARITY[1];
                                } else if (filter.equals(DND5E.SORTER_COST[0])) {
                                    filterField = DND5E.SORTER_COST[1];
                                } else if (filter.equals(DND5E.SORTERS_WEIGHT[0])) {
                                    filterField = DND5E.SORTERS_WEIGHT[1];
                                } else {
                                    filterField = DND5E.SORTER_NAME[1];
                                }

                                if (comboBoxFilter.getSelectionModel().getSelectedItem().equals(DND5E.FILTER_ANY)) {
                                    query = "SELECT name, rarity, weight, cost_copper, item_type FROM " + table + " ORDER BY " + filterField + " " + (sortDesc ? "DESC" : "ASC") + ";";
                                } else {
                                    query = "SELECT name, rarity, weight, cost_copper, item_type FROM " + table + " WHERE item_type = " + filterIndex + " ORDER BY " + filterField + " " + (sortDesc ? "DESC" : "ASC") + ";";
                                }

                            } else {
                                query = "SELECT name FROM " + table + " ORDER BY name "+(sortDesc?"DESC":"ASC")+";";
                            }
                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                    Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                });
                                return null;
                            }

                            ResultSet result = ps.executeQuery();

                            ArrayList<DND5EElementPreview> resultList = new ArrayList<>();

                            if (category.equals(DND5E.ITEMS[0])) {
                                while (result.next()) {
                                    resultList.add(
                                            new DND5EElementPreview(
                                                    result.getString("name"),
                                                    result.getDouble("cost_copper"),
                                                    result.getInt("rarity"),
                                                    result.getDouble("weight"),
                                                    result.getInt("item_type")
                                            )
                                    );
                                }
                            } else {
                                while (result.next()) {
                                    resultList.add(
                                        new DND5EElementPreview(
                                                result.getString("name"),
                                                0,
                                                0,
                                                0,
                                                0
                                        )
                                    );
                                }
                            }

                            ps.close();

                            Platform.runLater(() -> listViewOptions.setItems(FXCollections.observableList(resultList)));

                            return null;
                        } catch (Exception e) {
                            Logger.log(e);
                            throw e;
                        }
                    }
                };
            }
        };

        displaySelectedService.start();
    }
    @FXML
    private void search() {
        String category = comboBoxCategory.getSelectionModel().getSelectedItem();
        if (category == null) return;
        String userInput = textFieldSearchBar.getText();
        if (userInput == null || userInput.equals("")) {
            displaySelected();
            return;
        }
        Service<Void> searchService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            String table = getTableNameByCategory(category);
                            String query;
                            if (category.equals(DND5E.ITEMS[0])) {
                                int filterIndex = comboBoxFilter.getSelectionModel().getSelectedIndex()-1;
                                String filterField;
                                String filter = comboBoxSorter.getSelectionModel().getSelectedItem();
                                if (filter == null || filter.equals(DND5E.SORTER_NAME[0])) {
                                    filterField = DND5E.SORTER_NAME[1];
                                } else if (filter.equals(DND5E.SORTER_RARITY[0])) {
                                    filterField = DND5E.SORTER_RARITY[1];
                                } else if (filter.equals(DND5E.SORTER_COST[0])) {
                                    filterField = DND5E.SORTER_COST[1];
                                } else if (filter.equals(DND5E.SORTERS_WEIGHT[0])) {
                                    filterField = DND5E.SORTERS_WEIGHT[1];
                                } else {
                                    filterField = DND5E.SORTER_NAME[1];
                                }
                                if (comboBoxFilter.getSelectionModel().getSelectedItem().equals(DND5E.FILTER_ANY)) {
                                    query = "SELECT name, rarity, weight, cost_copper, item_type FROM " + table + " WHERE name LIKE '%" + userInput + "%' ORDER BY " + filterField + " " + (sortDesc ? "DESC" : "ASC") + ";";
                                } else {
                                    query = "SELECT name, rarity, weight, cost_copper, item_type FROM " + table + " WHERE name LIKE '%" + userInput + "%' AND item_type = " + filterIndex + " ORDER BY " + filterField + " " + (sortDesc ? "DESC" : "ASC") + ";";
                                }
                            } else {
                                query = "SELECT name FROM " + table + " WHERE name LIKE '%"+userInput+"%' ORDER BY name "+(sortDesc?"DESC":"ASC")+";";
                            }
                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                    Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                });
                                return null;
                            }

                            ResultSet result = ps.executeQuery();

                            ArrayList<DND5EElementPreview> resultList = new ArrayList<>();

                            if (category.equals(DND5E.ITEMS[0])) {
                                while (result.next()) {
                                    resultList.add(
                                            new DND5EElementPreview(
                                                    result.getString("name"),
                                                    result.getDouble("cost_copper"),
                                                    result.getInt("rarity"),
                                                    result.getDouble("weight"),
                                                    result.getInt("item_type")
                                            )
                                    );
                                }
                            } else {
                                while (result.next()) {
                                    resultList.add(
                                            new DND5EElementPreview(
                                                    result.getString("name"),
                                                    0,
                                                    0,
                                                    0,
                                                    0
                                            )
                                    );
                                }
                            }

                            ps.close();

                            Platform.runLater(() -> listViewOptions.setItems(FXCollections.observableList(resultList)));
                            return null;
                        } catch (Exception e) {
                            Logger.log(e);
                            throw e;
                        }
                    }
                };
            }
        };

        searchService.start();
    }
    @FXML
    private void searchOnEnter(@NotNull final KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            search();
        }
    }
    @FXML
    private void newElement() {
        elementName = null;
        thisScene = Client.getStage().getScene();
        String category = comboBoxCategory.getSelectionModel().getSelectedItem();
        if (category == null) return;
        if (category.equals(DND5E.ITEMS[0])) {

            String filter = comboBoxFilter.getSelectionModel().getSelectedItem();
            if (filter.equals(DND5E.FILTER_ANY)) {
                new ErrorAlert("ERRORE", "Errore di Inserimento", "Per poter creare un nuovo elemento, impostare un filtro");
                return;
            }

            if (filter.equals(DND5E.ITEMS[0])) {
                Client.getStage().setScene(SceneDND5EItem.getScene());
            } else if (filter.equals(DND5E.ARMORS[0])) {
                Client.getStage().setScene(SceneDND5EArmor.getScene());
            } else if (filter.equals(DND5E.WEAPONS[0])) {
                Client.getStage().setScene(SceneDND5EWeapon.getScene());
            } else if (filter.equals(DND5E.SPELLS[0])) {
                 Client.getStage().setScene(SceneDND5ESpell.getScene());
            } else if (filter.equals(DND5E.EQUIPMENT_PACKS[0])) {
                Client.getStage().setScene(SceneDND5EEquipmentPack.getScene());
            } else { // Shouldn't happen on full implemented app
                throw new RuntimeException("How is this even possible?");
            }
        } else if (category.equals(DND5E.TALENTS[0])) {
            Client.getStage().setScene(SceneDND5ETalent.getScene());
        } else if (category.equals(DND5E.BACKGROUNDS[0])) {
            Client.getStage().setScene(SceneDND5EBackground.getScene());
        } else if (category.equals(DND5E.LANGUAGES[0])) {
            Client.getStage().setScene(SceneDND5ELanguage.getScene());
        } else {
            // IMPLEMENT CLASS AND RACES
        }
    }
    @FXML
    private void editOnDoubleClick(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) editElement();
    }
    @FXML
    private void editElement() {
        if (listViewOptions.getSelectionModel().getSelectedItems().size() == 0) return;
        elementName = listViewOptions.getSelectionModel().getSelectedItem().getName();
        String category = comboBoxCategory.getSelectionModel().getSelectedItem();
        thisScene = Client.getStage().getScene();
        if (category.equals(DND5E.ITEMS[0])) {
            switch (listViewOptions.getSelectionModel().getSelectedItem().getType()) { // Refers to: ItemTypes Declaration Position
                case 0:
                    Client.getStage().setScene(SceneDND5EItem.getScene());
                    break;

                case 1:
                    Client.getStage().setScene(SceneDND5EArmor.getScene());
                    break;

                case 2:
                    Client.getStage().setScene(SceneDND5EWeapon.getScene());
                    break;

                case 3:
                    Client.getStage().setScene(SceneDND5ESpell.getScene());
                    break;

                case 4:
                    Client.getStage().setScene(SceneDND5EEquipmentPack.getScene());
                    break;

                default:
                    throw new RuntimeException("What is this item?");
            }
        } else if (category.equals(DND5E.TALENTS[0])) {
            Client.getStage().setScene(SceneDND5ETalent.getScene());
        } else if (category.equals(DND5E.BACKGROUNDS[0])) {
            Client.getStage().setScene(SceneDND5EBackground.getScene());
        } else if (category.equals(DND5E.LANGUAGES[0])) {
            Client.getStage().setScene(SceneDND5ELanguage.getScene());
        } else {
            // TODO: handle class and races
        }
    }
    @FXML
    private void deleteElement() {
        if (listViewOptions.getSelectionModel().getSelectedItems().size() == 0) return;
        String choice = comboBoxCategory.getSelectionModel().getSelectedItem();
        String elementName = listViewOptions.getSelectionModel().getSelectedItem().getName();
        String table = getTableNameByCategory(choice);
        Service<Void> deleteElementService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            String query = "DELETE FROM " + table + " WHERE name = ?;";
                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                    Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                });
                                return null;
                            }
                            ps.setString(1, elementName);
                            ps.executeUpdate();
                            ps.close();
                            Platform.runLater(ControllerSceneDND5EList.this::search);
                            return null;
                        } catch (Exception e) {
                            Logger.log(e);
                            throw e;
                        }
                    }
                };
            }
        };

        deleteElementService.start();
    }

    // Methods
    private String getTableNameByCategory(@NotNull final String category) {
        if (category.equals(DND5E.ITEMS[0])) {
            return DND5E.ITEMS[1];
        } else if (category.equals(DND5E.CLASSES[0])) {
            return DND5E.CLASSES[1];
        } else if (category.equals(DND5E.RACES[0])) {
            return DND5E.RACES[1];
        } else if (category.equals(DND5E.BACKGROUNDS[0])) {
            return DND5E.BACKGROUNDS[1];
        } else if (category.equals(DND5E.TALENTS[0])) {
            return DND5E.TALENTS[1];
        } else if (category.equals(DND5E.LANGUAGES[0])) {
            return DND5E.LANGUAGES[1];
        }else {
            throw new RuntimeException("Choice not supported!");
        }
    }
    @Nullable
    public static String getElementName() {
        return elementName;
    }
    @Nullable
    public static Scene getListScene() {
        return thisScene;
    }
}
