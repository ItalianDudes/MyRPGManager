package it.italiandudes.myrpgmanager.javafx.controller.dnd5e;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager.Defs.SupportedRPGs.DND5E;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    //Graphic Elements
    @FXML private ComboBox<String> comboBoxFilter;
    @FXML private TextField textFieldSearchBar;
    @FXML private ListView<String> listViewOptions;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        comboBoxFilter.setItems(FXCollections.observableList(DND5E.ELEMENTS));
    }

    // EDT
    @FXML
    private void displaySelected() {
        String choice = comboBoxFilter.getSelectionModel().getSelectedItem();
        Service<Void> displaySelectedService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            String table = getTableNameByFilter(choice);
                            String query = "SELECT name FROM " + table + ";";
                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non è stato possibile consultare il database");
                                    Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                });
                                return null;
                            }

                            ResultSet result = ps.executeQuery();

                            ArrayList<String> resultList = new ArrayList<>();

                            while (result.next()) {
                                resultList.add(result.getString("name"));
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
        String choice = comboBoxFilter.getSelectionModel().getSelectedItem();
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
                            String query = "SELECT name FROM " + table + " WHERE name LIKE '%"+userInput+"%' ORDER BY name;";
                            // fixme: Understand how to implement the search that contains the world
                            PreparedStatement ps = DBManager.preparedStatement(query);
                            if (ps == null) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non è stato possibile consultare il database");
                                    Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                                });
                                return null;
                            }

                            //ps.setString(1, userInput);
                            ResultSet result = ps.executeQuery();

                            ArrayList<String> resultList = new ArrayList<>();

                            while (result.next()) {
                                resultList.add(result.getString("name"));
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
        if (event.getCharacter().equals("\n")) {
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
        elementName = listViewOptions.getSelectionModel().getSelectedItem();
        thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneDND5EItem.getScene());
    }
    @FXML
    private void deleteElement() {
        if (listViewOptions.getSelectionModel().getSelectedItems().size() == 0) return;
        String choice = comboBoxFilter.getSelectionModel().getSelectedItem();
        String elementName = listViewOptions.getSelectionModel().getSelectedItem();
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
                                    new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non è stato possibile consultare il database");
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
