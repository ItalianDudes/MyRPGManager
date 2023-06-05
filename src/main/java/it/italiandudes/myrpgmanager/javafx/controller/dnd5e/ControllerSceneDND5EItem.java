package it.italiandudes.myrpgmanager.javafx.controller.dnd5e;

import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.data.Item;
import it.italiandudes.myrpgmanager.data.Rarity;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.JFXDefs;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.alert.InformationAlert;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

@SuppressWarnings("unused")
public final class ControllerSceneDND5EItem {

    // Attributes
    private Item item = null;
    private String imageExtension = null;
    private static final Image defaultImage;
    static {
        try {
            defaultImage = SwingFXUtils.toFXImage(ImageIO.read(MyRPGManager.Defs.Resources.getAsStream(JFXDefs.Resource.Image.IMAGE_LOGO)), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
        imageViewItem.setImage(defaultImage);
        comboBoxRarity.setItems(FXCollections.observableList(Rarity.colorNames));
        comboBoxRarity.getSelectionModel().selectFirst();
        comboBoxRarity.buttonCellProperty().bind(Bindings.createObjectBinding(() -> {

            Rarity identifiedRarity = null;
            for (Rarity rarity : Rarity.values()) {
                if (rarity.getTextedRarity().equals(comboBoxRarity.getSelectionModel().getSelectedItem())) {
                    identifiedRarity = rarity;
                }
            }

            if (identifiedRarity == null) return null;

            final Color color = identifiedRarity.getColor();

            // Get the arrow button of the combo-box
            StackPane arrowButton = (StackPane) comboBoxRarity.lookup(".arrow-button");
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setBackground(Background.EMPTY);
                        setText("");
                    } else {
                        setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                        setText(item);
                    }
                    // Set the background of the arrow also
                    if (arrowButton != null)
                        arrowButton.setBackground(getBackground());
                }
            };
        }, comboBoxRarity.valueProperty()));
        String itemName = ControllerSceneDND5EList.getElementName();
        if (itemName != null) {
            initExistingItem(itemName);
        }
    }

    // EDT
    @FXML
    private void removeImage() {
        imageViewItem.setImage(defaultImage);
        imageExtension = null;
    }
    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un Contenuto Multimediale");
        for (String extension : MyRPGManager.Defs.Resources.SQL.SUPPORTED_IMAGE_EXTENSIONS) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension, "*."+extension));
        }
        fileChooser.setInitialDirectory(new File(MyRPGManager.Defs.JAR_POSITION).getParentFile());
        File imagePath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        if(imagePath!=null) {
            Service<Void> imageReaderService = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() {
                            try {
                                BufferedImage img = ImageIO.read(imagePath);
                                Platform.runLater(() -> imageViewItem.setImage(SwingFXUtils.toFXImage(img, null)));
                                imageExtension = ImageHandler.getImageExtension(imagePath.getAbsolutePath());
                            }catch (IOException e) {
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Lettura", "Impossibile leggere il contenuto selezionato."));
                            }
                            return null;
                        }
                    };
                }
            };
            imageReaderService.start();
        }
    }
    @FXML
    private void backToElementList() {
        Client.getStage().setScene(ControllerSceneDND5EList.getListScene());
    }
    @FXML
    private void save() {
        if (textFieldName.getText().replace(" ", "").equals("")) {
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome all'oggetto.");
            return;
        }
        Service<Void> saveService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            if (item == null) {
                                String query = "INSERT INTO items (name, base64image, image_extension, cost_copper, description, rarity, weight) VALUES (?, ?, ?, ?, ?, ?, ?);";

                                PreparedStatement ps = DBManager.preparedStatement(query);
                                if (ps == null) {
                                    throw new SQLException("Database connection doesn't exist");
                                }

                                ps.setString(1, textFieldName.getText());
                                if (!imageViewItem.getImage().equals(defaultImage)) {
                                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageViewItem.getImage(), null);
                                    ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
                                    ImageIO.write(bufferedImage, imageExtension, imageByteStream);
                                    ps.setString(2, Base64.getEncoder().encodeToString(imageByteStream.toByteArray()));

                                    ps.setString(3, imageExtension);
                                } else {
                                    ps.setString(2, null);
                                    ps.setString(3, null);
                                }

                                int copperCoins;

                                try {
                                    copperCoins = Integer.parseInt(textFieldMR.getText()) + (Integer.parseInt(textFieldMA.getText()) * 10) + (Integer.parseInt(textFieldME.getText()) * 50) + (Integer.parseInt(textFieldMO.getText()) * 100) + (Integer.parseInt(textFieldMP.getText()) * 1000);
                                } catch (NumberFormatException e) {
                                    copperCoins = 0;
                                }

                                ps.setInt(4, copperCoins);
                                ps.setString(5, textAreaDescription.getText());
                                ps.setInt(6, Rarity.colorNames.indexOf(comboBoxRarity.getSelectionModel().getSelectedItem()));
                                try {
                                    ps.setDouble(7, Double.parseDouble(textFieldWeight.getText()));
                                } catch (NumberFormatException e) {
                                    ps.setDouble(7, 0);
                                }

                                ps.executeUpdate();

                                ps.close();

                                Platform.runLater(() -> new InformationAlert("SUCCESSO", "Inserimento Dati", "Inserimento dei dati effettuato con successo!"));

                            } else {
                                String query = "UPDATE items SET name=?, base64image=?, image_extension=?, cost_copper=?, description=?, rarity=?, weight=? WHERE name=?;";

                                PreparedStatement ps = DBManager.preparedStatement(query);
                                if (ps == null) {
                                    throw new SQLException("Database connection doesn't exist");
                                }

                                ps.setString(1, textFieldName.getText());
                                if (!imageViewItem.getImage().equals(defaultImage)) {
                                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageViewItem.getImage(), null);
                                    ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
                                    ImageIO.write(bufferedImage, imageExtension, imageByteStream);
                                    ps.setString(2, Base64.getEncoder().encodeToString(imageByteStream.toByteArray()));
                                    ps.setString(3, imageExtension);
                                } else {
                                    ps.setString(2, null);
                                    ps.setString(3, null);
                                }

                                int copperCoins;

                                try {
                                    copperCoins = Integer.parseInt(textFieldMR.getText()) + (Integer.parseInt(textFieldMA.getText()) * 10) + (Integer.parseInt(textFieldME.getText()) * 50) + (Integer.parseInt(textFieldMO.getText()) * 100) + (Integer.parseInt(textFieldMP.getText()) * 1000);
                                } catch (NumberFormatException e) {
                                    copperCoins = 0;
                                }

                                ps.setInt(4, copperCoins);
                                ps.setString(5, textAreaDescription.getText());
                                ps.setInt(6, Rarity.colorNames.indexOf(comboBoxRarity.getSelectionModel().getSelectedItem()));
                                try {
                                    ps.setDouble(7, Double.parseDouble(textFieldWeight.getText()));
                                } catch (NumberFormatException e) {
                                    ps.setDouble(7, 0);
                                }
                                ps.setString(8, item.getName());

                                ps.executeUpdate();

                                ps.close();

                                Platform.runLater(() -> new InformationAlert("SUCCESSO", "Aggiornamento Dati", "Aggiornamento dei dati effettuato con successo!"));
                            }
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
    private void initExistingItem(@NotNull final String itemName) {
        Service<Void> itemInitializerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {

                            item = new Item(itemName);

                            imageExtension = item.getImageExtension();
                            int CC = item.getCostCopper();
                            int CP = CC / 1000;
                            CC -= CP * 1000;
                            int CG = CC / 100;
                            CC -= CG * 100;
                            int CE = CC / 50;
                            CC -= CE * 50;
                            int CS = CC / 10;
                            CC -= CS * 10;

                            BufferedImage bufferedImage = null;
                            try {
                                if (item.getBase64image() != null && imageExtension != null) {
                                    byte[] imageBytes = Base64.getDecoder().decode(item.getBase64image());
                                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                    bufferedImage = ImageIO.read(imageStream);
                                } else if (item.getBase64image() != null && imageExtension == null) {
                                    throw new IllegalArgumentException("Image without declared extension");
                                }
                            } catch (IllegalArgumentException e) {
                                Platform.runLater(() -> {
                                    new ErrorAlert("ERRORE", "Errore di lettura", "L'immagine ricevuta dal database non è leggibile");
                                    Client.getStage().setScene(ControllerSceneDND5EList.getListScene());
                                });
                                return null;
                            }

                            int finalCC = CC;
                            BufferedImage finalBufferedImage = bufferedImage;
                            Platform.runLater(() -> {

                                textFieldName.setText(item.getName());
                                textFieldWeight.setText(String.valueOf(item.getWeight()));
                                comboBoxRarity.getSelectionModel().select(item.getRarity().getTextedRarity());
                                textFieldMR.setText(String.valueOf(finalCC));
                                textFieldMA.setText(String.valueOf(CS));
                                textFieldME.setText(String.valueOf(CE));
                                textFieldMO.setText(String.valueOf(CG));
                                textFieldMP.setText(String.valueOf(CP));
                                textAreaDescription.setText(item.getDescription());
                                if (finalBufferedImage != null)
                                    imageViewItem.setImage(SwingFXUtils.toFXImage(finalBufferedImage, null));
                                else
                                    imageViewItem.setImage(new Image(MyRPGManager.Defs.Resources.getAsStream(JFXDefs.Resource.Image.IMAGE_LOGO)));

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

        itemInitializerService.start();
    }
}
