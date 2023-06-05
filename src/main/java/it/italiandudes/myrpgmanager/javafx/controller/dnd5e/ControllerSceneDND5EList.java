package it.italiandudes.myrpgmanager.javafx.controller.dnd5e;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager.Defs.SupportedRPGs.DND5E;
import it.italiandudes.myrpgmanager.data.ElementPreview;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.scene.SceneCreateOrChooseDB;
import it.italiandudes.myrpgmanager.javafx.scene.dnd5e.SceneDND5EItem;
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
    @FXML private ComboBox<String> comboBoxFilter;
    @FXML private TextField textFieldSearchBar;
    @FXML private CheckBox checkBoxSortDesc;
    @FXML private ListView<ElementPreview> listViewOptions;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        comboBoxCategory.setItems(FXCollections.observableList(DND5E.ELEMENTS));
        comboBoxFilter.setItems(FXCollections.observableList(DND5E.FILTERS));
        comboBoxFilter.setVisible(false);
        listViewOptions.setCellFactory(lv -> new ListCell<ElementPreview>() {
            @Override
            protected void updateItem(ElementPreview elementPreview, boolean empty) {
                super.updateItem(elementPreview, empty);
                if (empty) {
                    setText(null);
                    setStyle("-fx-font-weight: bold;");
                } else {
                    if (comboBoxCategory.getSelectionModel().getSelectedItem().equals(DND5E.ITEMS[0])) {
                        setText(elementPreview.getName());
                        assert elementPreview.getRarity() != null;
                        setStyle("-fx-background-color: "+ elementPreview.getRarityColor()+";-fx-font-weight: bold;");
                    } else {
                        setText(elementPreview.getName());
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
        if (!category.equals(DND5E.ITEMS[0])) return;
        Service<Void> filterSearchService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            String filterField;
                            String filter = comboBoxFilter.getSelectionModel().getSelectedItem();
                            if (filter == null || filter.equals(DND5E.FILTER_NAME[0])) {
                                filterField = DND5E.FILTER_NAME[1];
                            } else if (filter.equals(DND5E.FILTER_RARITY[0])) {
                                filterField = DND5E.FILTER_RARITY[1];
                            } else if (filter.equals(DND5E.FILTER_COST[0])) {
                                filterField = DND5E.FILTER_COST[1];
                            } else if (filter.equals(DND5E.FILTER_WEIGHT[0])) {
                                filterField = DND5E.FILTER_WEIGHT[1];
                            } else {
                                filterField = DND5E.FILTER_NAME[1];
                            }

                            String query = "SELECT name, rarity, weight, cost_copper FROM items ORDER BY "+filterField+" "+(sortDesc?"DESC":"ASC")+";";

                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non e' stato possibile consultare il database");
                                    Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                });
                                return null;
                            }

                            ResultSet result = ps.executeQuery();

                            ArrayList<ElementPreview> resultList = new ArrayList<>();

                            while (result.next()) {
                                resultList.add(
                                        new ElementPreview(
                                                result.getString("name"),
                                                result.getDouble("cost_copper"),
                                                result.getInt("rarity"),
                                                result.getDouble("weight")
                                        )
                                );
                            }

                            ps.close();

                            Platform.runLater(() -> listViewOptions.setItems(FXCollections.observableList(resultList)));
                        } catch (Exception e) {
                            Logger.log(e);
                            throw e;
                        }
                        return null;
                    }
                };
            }
        };
        filterSearchService.start();
    }
    @FXML
    private void displaySelected() {
        String choice = comboBoxCategory.getSelectionModel().getSelectedItem();
        comboBoxFilter.setVisible(choice.equals(DND5E.ITEMS[0]));
        Service<Void> displaySelectedService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            String table = getTableNameByFilter(choice);
                            String query;
                            if (choice.equals(DND5E.ITEMS[0])) {
                                String filterField;
                                String filter = comboBoxFilter.getSelectionModel().getSelectedItem();
                                if (filter == null || filter.equals(DND5E.FILTER_NAME[0])) {
                                    filterField = DND5E.FILTER_NAME[1];
                                } else if (filter.equals(DND5E.FILTER_RARITY[0])) {
                                    filterField = DND5E.FILTER_RARITY[1];
                                } else if (filter.equals(DND5E.FILTER_COST[0])) {
                                    filterField = DND5E.FILTER_COST[1];
                                } else if (filter.equals(DND5E.FILTER_WEIGHT[0])) {
                                    filterField = DND5E.FILTER_WEIGHT[1];
                                } else {
                                    filterField = DND5E.FILTER_NAME[1];
                                }

                                query = "SELECT name, rarity, weight, cost_copper FROM " + table + " ORDER BY "+filterField+" "+(sortDesc?"DESC":"ASC")+";";
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

                            ArrayList<ElementPreview> resultList = new ArrayList<>();

                            if (choice.equals(DND5E.ITEMS[0])) {
                                while (result.next()) {
                                    resultList.add(
                                            new ElementPreview(
                                                    result.getString("name"),
                                                    result.getDouble("cost_copper"),
                                                    result.getInt("rarity"),
                                                    result.getDouble("weight")
                                            )
                                    );
                                }
                            } else {
                                while (result.next()) {
                                    resultList.add(
                                        new ElementPreview(
                                                result.getString("name"),
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
        String choice = comboBoxCategory.getSelectionModel().getSelectedItem();
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
                            String table = getTableNameByFilter(choice);
                            String query;
                            if (choice.equals(DND5E.ITEMS[0])) {
                                String filterField;
                                String filter = comboBoxFilter.getSelectionModel().getSelectedItem();
                                if (filter == null || filter.equals(DND5E.FILTER_NAME[0])) {
                                    filterField = DND5E.FILTER_NAME[1];
                                } else if (filter.equals(DND5E.FILTER_RARITY[0])) {
                                    filterField = DND5E.FILTER_RARITY[1];
                                } else if (filter.equals(DND5E.FILTER_COST[0])) {
                                    filterField = DND5E.FILTER_COST[1];
                                } else if (filter.equals(DND5E.FILTER_WEIGHT[0])) {
                                    filterField = DND5E.FILTER_WEIGHT[1];
                                } else {
                                    filterField = DND5E.FILTER_NAME[1];
                                }
                                query = "SELECT name, rarity, weight, cost_copper FROM " + table + " WHERE "+filterField+" LIKE '%"+userInput+"%' ORDER BY name "+(sortDesc?"DESC":"ASC")+";";
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

                            ArrayList<ElementPreview> resultList = new ArrayList<>();

                            if (choice.equals(DND5E.ITEMS[0])) {
                                while (result.next()) {
                                    resultList.add(
                                            new ElementPreview(
                                                    result.getString("name"),
                                                    result.getDouble("cost_copper"),
                                                    result.getInt("rarity"),
                                                    result.getDouble("weight")
                                            )
                                    );
                                }
                            } else {
                                while (result.next()) {
                                    resultList.add(
                                            new ElementPreview(
                                                    result.getString("name"),
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
        Client.getStage().setScene(SceneDND5EItem.getScene());
    }
    @FXML
    private void editOnDoubleClick(@NotNull final MouseEvent event) {
        if (event.getClickCount() >= 2) editElement();
    }
    @FXML
    private void editElement() {
        if (listViewOptions.getSelectionModel().getSelectedItems().size() == 0) return;
        elementName = listViewOptions.getSelectionModel().getSelectedItem().getName();
        thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneDND5EItem.getScene());
    }
    @FXML
    private void deleteElement() {
        if (listViewOptions.getSelectionModel().getSelectedItems().size() == 0) return;
        String choice = comboBoxCategory.getSelectionModel().getSelectedItem();
        String elementName = listViewOptions.getSelectionModel().getSelectedItem().getName();
        String table = getTableNameByFilter(choice);
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
    private String getTableNameByFilter(@NotNull final String filter) {
        if (filter.equals(DND5E.ITEMS[0])) {
            return DND5E.ITEMS[1];
        } else if (filter.equals(DND5E.CLASSES[0])) {
            return DND5E.CLASSES[1];
        } else if (filter.equals(DND5E.RACES[0])) {
            return DND5E.RACES[1];
        } else if (filter.equals(DND5E.BACKGROUNDS[0])) {
            return DND5E.BACKGROUNDS[1];
        } else if (filter.equals(DND5E.TALENTS[0])) {
            return DND5E.TALENTS[1];
        } else if (filter.equals(DND5E.LANGUAGES[0])) {
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
