package it.italiandudes.myrpgmanager.javafx.controller.dnd5e.item;

import it.italiandudes.idl.common.ImageHandler;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.MyRPGManager;
import it.italiandudes.myrpgmanager.MyRPGManager.Defs.SupportedRPGs.DND5E;
import it.italiandudes.myrpgmanager.data.dnd5e.item.DND5EArmor;
import it.italiandudes.myrpgmanager.data.dnd5e.item.DND5EItem;
import it.italiandudes.myrpgmanager.data.dnd5e.item.DND5EItemTypes;
import it.italiandudes.myrpgmanager.data.common.Rarity;
import it.italiandudes.myrpgmanager.javafx.Client;
import it.italiandudes.myrpgmanager.javafx.alert.ErrorAlert;
import it.italiandudes.myrpgmanager.javafx.alert.InformationAlert;
import it.italiandudes.myrpgmanager.javafx.controller.dnd5e.ControllerSceneDND5EList;
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
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@SuppressWarnings("unused")
public final class ControllerSceneDND5EArmor {

    // Attributes
    private DND5EArmor DND5EArmor = null;
    private String imageExtension = null;
    private boolean isImageSet = false;
    private static final Image defaultImage = Client.getDefaultImage();

    // Graphic Elements
    @FXML private TextField textFieldName;
    @FXML private TextField textFieldWeight;
    @FXML private ComboBox<String> comboBoxRarity;
    @FXML private TextField textFieldMR;
    @FXML private TextField textFieldMA;
    @FXML private TextField textFieldME;
    @FXML private TextField textFieldMO;
    @FXML private TextField textFieldMP;
    @FXML private TextField textFieldCategory;
    @FXML private TextField textFieldAC;
    @FXML private TextField textFieldStrengthRequired;
    @FXML private ComboBox<String> comboBoxStealth;
    @FXML private TextArea textAreaDescription;
    @FXML private ImageView imageViewItem;

    // Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
        imageViewItem.setImage(defaultImage);
        comboBoxRarity.setItems(FXCollections.observableList(Rarity.colorNames));
        comboBoxRarity.getSelectionModel().selectFirst();
        comboBoxStealth.setItems(FXCollections.observableList(DND5E.STEALTHS));
        comboBoxStealth.getSelectionModel().selectFirst();
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
        String armorName = ControllerSceneDND5EList.getElementName();
        if (armorName != null) {
            initExistingArmor(armorName);
        }
    }

    // EDT
    @FXML
    private void removeImage() {
        imageViewItem.setImage(defaultImage);
        imageExtension = null;
        isImageSet = false;
    }
    @FXML
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un Contenuto Multimediale");
        for (String extension : MyRPGManager.Defs.Resources.SQL.SUPPORTED_IMAGE_EXTENSIONS) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension, "*."+extension));
        }
        fileChooser.setInitialDirectory(new File(MyRPGManager.Defs.JAR_POSITION).getParentFile());
        File imagePath;
        try {
            imagePath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        } catch (IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            imagePath = fileChooser.showOpenDialog(Client.getStage().getScene().getWindow());
        }
        if(imagePath!=null) {
            File finalImagePath = imagePath;
            Service<Void> imageReaderService = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() {
                            try {
                                BufferedImage img = ImageIO.read(finalImagePath);
                                Platform.runLater(() -> imageViewItem.setImage(SwingFXUtils.toFXImage(img, null)));
                                imageExtension = ImageHandler.getImageExtension(finalImagePath.getAbsolutePath());
                                isImageSet = true;
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
            new ErrorAlert("ERRORE", "Errore di Inserimento", "Non e' stato assegnato un nome all'armatura.");
            return;
        }
        Service<Void> saveService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            String stealthChoice = comboBoxStealth.getSelectionModel().getSelectedItem();
                            int stealth;
                            if (stealthChoice.equals(MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_NEUTRAL[0])) {
                                stealth = Integer.parseInt(MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_NEUTRAL[1]);
                            } else if (stealthChoice.equals(MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_ADVANTAGE[0])) {
                                stealth = Integer.parseInt(MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_ADVANTAGE[1]);
                            } else if (stealthChoice.equals(MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_DISADVANGE[0])) {
                                stealth = Integer.parseInt(MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_DISADVANGE[1]);
                            } else {
                                throw new RuntimeException("How this is even possible?");
                            }
                            double weight;
                            try {
                                String textWeight = textFieldWeight.getText();
                                if (textWeight == null || textWeight.replace(" ", "").equals("")) {
                                    weight = 0;
                                } else {
                                    weight = Double.parseDouble(textFieldWeight.getText());
                                    if (weight < 0) throw new NumberFormatException("The weight is less than 0");
                                }
                            } catch (NumberFormatException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Il peso deve essere un numero a virgola mobile positivo!"));
                                return null;
                            }
                            String oldName = null;
                            int mr, ma, me, mo, mp;
                            try {
                                String strMR = textFieldMR.getText();
                                if (strMR == null || strMR.replace(" ", "").equals("")) {
                                    mr = 0;
                                } else {
                                    mr = Integer.parseInt(strMR);
                                }
                                String strMA = textFieldMA.getText();
                                if (strMA == null || strMA.replace(" ", "").equals("")) {
                                    ma = 0;
                                } else {
                                    ma = Integer.parseInt(strMA);
                                }
                                String strME = textFieldME.getText();
                                if (strME == null || strME.replace(" ", "").equals("")) {
                                    me = 0;
                                } else {
                                    me = Integer.parseInt(strME);
                                }
                                String strMO = textFieldMO.getText();
                                if (strMO == null || strMO.replace(" ", "").equals("")) {
                                    mo = 0;
                                } else {
                                    mo = Integer.parseInt(strMO);
                                }
                                String strMP = textFieldMP.getText();
                                if (strMP == null || strMP.replace(" ", "").equals("")) {
                                    mp = 0;
                                } else {
                                    mp = Integer.parseInt(strMP);
                                }
                                if (mr < 0 || ma < 0 || me < 0 || mo < 0 || mp < 0) throw new NumberFormatException("A number is negative");
                            } catch (NumberFormatException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Le valute devono essere dei numeri interi positivi!"));
                                return null;
                            }
                            int ac, strengthRequired;
                            try {
                                String strAC = textFieldAC.getText();
                                if (strAC == null || strAC.replace(" ", "").equals("")) {
                                    ac = 0;
                                } else {
                                    ac = Integer.parseInt(strAC);
                                }
                                if (ac < 0) throw new NumberFormatException("The AC is negative");
                            } catch (NumberFormatException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "La CA deve essere un numero intero positivo!"));
                                return null;
                            }
                            try {
                                String strStrengthRequired = textFieldStrengthRequired.getText();
                                if (strStrengthRequired == null || strStrengthRequired.replace(" ", "").equals("")) {
                                    strengthRequired = 0;
                                } else {
                                    strengthRequired = Integer.parseInt(strStrengthRequired);
                                }
                            } catch (NumberFormatException e) {
                                Logger.log(e);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "La forza richiesta deve essere un numero intero positivo!"));
                                return null;
                            }
                            if (DND5EArmor == null) {
                                if (DND5EItem.checkIfExist(textFieldName.getText())) {
                                    Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di Inserimento", "Esiste gia' qualcosa con questo nome registrato!"));
                                    return null;
                                }
                                DND5EItem DND5EItem = new DND5EItem(
                                        null,
                                        imageViewItem.getImage(),
                                        imageExtension,
                                        textFieldName.getText(),
                                        mr,
                                        ma,
                                        me,
                                        mo,
                                        mp,
                                        textAreaDescription.getText(),
                                        comboBoxRarity.getSelectionModel().getSelectedItem(),
                                        DND5EItemTypes.TYPE_ARMOR.getDatabaseValue(),
                                        weight
                                );
                                DND5EArmor = new DND5EArmor(
                                        DND5EItem,
                                        null,
                                        textFieldCategory.getText(),
                                        ac,
                                        strengthRequired,
                                        stealth
                                );
                            } else {
                                oldName = DND5EArmor.getName();
                                DND5EItem DND5EItem = new DND5EItem(
                                        DND5EArmor.getItemID(),
                                        imageViewItem.getImage(),
                                        imageExtension,
                                        textFieldName.getText(),
                                        mr,
                                        ma,
                                        me,
                                        mo,
                                        mp,
                                        textAreaDescription.getText(),
                                        comboBoxRarity.getSelectionModel().getSelectedItem(),
                                        DND5EItemTypes.TYPE_ARMOR.getDatabaseValue(),
                                        weight
                                );
                                DND5EArmor = new DND5EArmor(
                                        DND5EItem,
                                        DND5EArmor.getArmorID(),
                                        textFieldCategory.getText(),
                                        ac,
                                        strengthRequired,
                                        stealth
                                );
                            }

                            DND5EArmor.saveIntoDatabase(oldName);
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Aggiornamento Dati", "Aggiornamento dei dati effettuato con successo!"));
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
    private void initExistingArmor(@NotNull final String armorName) {
        Service<Void> itemInitializerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {

                            DND5EArmor = new DND5EArmor(armorName);

                            imageExtension = DND5EArmor.getImageExtension();
                            int CC = DND5EArmor.getCostCopper();
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
                                if (DND5EArmor.getBase64image() != null && imageExtension != null) {
                                    byte[] imageBytes = Base64.getDecoder().decode(DND5EArmor.getBase64image());
                                    ByteArrayInputStream imageStream = new ByteArrayInputStream(imageBytes);
                                    bufferedImage = ImageIO.read(imageStream);
                                } else if (DND5EArmor.getBase64image() != null && imageExtension == null) {
                                    throw new IllegalArgumentException("Image without declared extension");
                                }
                            } catch (IllegalArgumentException e) {
                                Logger.log(e);
                                DND5EArmor.setBase64image(null);
                                DND5EArmor.setImageExtension(null);
                                Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di lettura", "L'immagine ricevuta dal database non è leggibile"));
                                return null;
                            }

                            int finalCC = CC;
                            BufferedImage finalBufferedImage = bufferedImage;
                            Platform.runLater(() -> {

                                textFieldName.setText(DND5EArmor.getName());
                                textFieldWeight.setText(String.valueOf(DND5EArmor.getWeight()));
                                comboBoxRarity.getSelectionModel().select(DND5EArmor.getRarity().getTextedRarity());
                                textFieldMR.setText(String.valueOf(finalCC));
                                textFieldMA.setText(String.valueOf(CS));
                                textFieldME.setText(String.valueOf(CE));
                                textFieldMO.setText(String.valueOf(CG));
                                textFieldMP.setText(String.valueOf(CP));
                                textAreaDescription.setText(DND5EArmor.getDescription());
                                if (finalBufferedImage != null && imageExtension != null) {
                                    imageViewItem.setImage(SwingFXUtils.toFXImage(finalBufferedImage, null));
                                    isImageSet = true;
                                } else {
                                    imageViewItem.setImage(defaultImage);
                                }

                                String stealthStr;
                                if (DND5EArmor.getStealth() == -1) {
                                    stealthStr = MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_DISADVANGE[0];
                                } else if (DND5EArmor.getStealth() == 0) {
                                    stealthStr = MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_NEUTRAL[0];
                                } else if (DND5EArmor.getStealth() == 1) {
                                    stealthStr = MyRPGManager.Defs.SupportedRPGs.DND5E.STEALTH_ADVANTAGE[0];
                                } else {
                                    throw new RuntimeException("How this is even possible?");
                                }
                                textFieldCategory.setText(DND5EArmor.getCategory());
                                comboBoxStealth.getSelectionModel().select(stealthStr);
                                textFieldAC.setText(String.valueOf(DND5EArmor.getAC()));
                                textFieldStrengthRequired.setText(String.valueOf(DND5EArmor.getStrengthRequired()));
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
