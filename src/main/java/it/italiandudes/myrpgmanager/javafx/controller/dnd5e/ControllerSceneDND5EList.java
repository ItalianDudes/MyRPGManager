package it.italiandudes.myrpgmanager.javafx.controller.dnd5e;

import it.italiandudes.myrpgmanager.MyRPGManager.Defs.SupportedRPGs.DND5E;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.scene.SceneCreateOrChooseDB;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@SuppressWarnings("unused")
public final class ControllerSceneDND5EList {

    // Attributes
    private String filter = null;

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
    private void refresh() {
        Platform.runLater(() -> {
            comboBoxFilter.getSelectionModel().select(filter);
            displaySelected();
        });
    }
    @FXML
    private void displaySelected() {
        String choice = comboBoxFilter.getSelectionModel().getSelectedItem();
        Service<Void> displaySelectedService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String table = getTableNameByFilter(choice);
                        String query = "SELECT name FROM "+table+";";
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
                    }
                };
            }
        };

        displaySelectedService.start();
    }
    @FXML
    private void search() {
        String choice = comboBoxFilter.getSelectionModel().getSelectedItem();
        // String userInput = '%' + textFieldSearchBar.getText().replace("%", "[%]") + '%';
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
                        String table = getTableNameByFilter(choice);
                        String query = "SELECT name FROM "+table+" WHERE name LIKE '%?%';";
                        PreparedStatement ps = DBManager.preparedStatement(query);
                        if (ps == null) {
                            Platform.runLater(() -> {
                                new ErrorAlert("ERRORE", "Errore di Connessione al database", "Non è stato possibile consultare il database");
                                Client.getStage().setScene(SceneCreateOrChooseDB.getScene());
                            });
                            return null;
                        }

                        ps.setString(1, userInput);
                        ResultSet result = ps.executeQuery();

                        ArrayList<String> resultList = new ArrayList<>();

                        while (result.next()) {
                            resultList.add(result.getString("name"));
                        }

                        ps.close();

                        Platform.runLater(() -> listViewOptions.setItems(FXCollections.observableList(resultList)));

                        return null;
                    }
                };
            }
        };

        searchService.start();
    }
    @FXML
    private void searchOnEnter(@NotNull final KeyEvent event) {
        if (event.getKeyChar() == '\n') {
            search();
        }
    }
    @FXML
    private void newElement() {

    }
    @FXML
    private void editElement() {

    }
    @FXML
    private void deleteElement() {
        String choice = comboBoxFilter.getSelectionModel().getSelectedItem();
        String elementName = listViewOptions.getSelectionModel().getSelectedItem();
        String table = getTableNameByFilter(choice);
        Service<Void> deleteElementService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String query = "DELETE FROM "+table+" WHERE name = ?;";
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
    public void setFilter(@NotNull final String filter) {
        this.filter = filter;
        refresh();
    }

}
