package auto_ui.symulatorgui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import symulator.*;

public class DodajSamochodController {

    @FXML private TextField modelTextField;
    @FXML private TextField registrationTextField;
    @FXML private TextField weightTextField;
    @FXML private TextField speedTextField;
    @FXML private ComboBox<String> silnikComboBox;
    @FXML private TextField maxObrotyTextField;
    @FXML private ComboBox<String> skrzyniaComboBox;
    @FXML private TextField liczbaBiegowTextField;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton;
    @FXML private Label statusLabel;

    private HelloController mainController;

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        System.out.println("DodajSamochodController initialized");

        initializeSilnikComboBox();
        initializeSkrzyniaComboBox();

        silnikComboBox.setOnAction(event -> onSilnikSelected());
        skrzyniaComboBox.setOnAction(event -> onSkrzyniaSelected());

        onSilnikSelected();
        onSkrzyniaSelected();
    }

    private void initializeSilnikComboBox() {
        silnikComboBox.getItems().addAll(
                "Silnik benzynowy 1.6 (120 KM)",
                "Silnik benzynowy 2.0 (150 KM)",
                "Silnik diesel 2.0 (136 KM)",
                "Silnik elektryczny (200 KM)"
        );
        silnikComboBox.setValue("Silnik benzynowy 1.6 (120 KM)");
    }

    private void initializeSkrzyniaComboBox() {
        skrzyniaComboBox.getItems().addAll(
                "Skrzynia manualna 5-biegowa",
                "Skrzynia manualna 6-biegowa",
                "Skrzynia automatyczna 6-biegowa",
                "Skrzynia automatyczna 8-biegowa"
        );
        skrzyniaComboBox.setValue("Skrzynia manualna 5-biegowa");
    }

    private void onSilnikSelected() {
        String wybrany = silnikComboBox.getValue();
        if (wybrany != null) {
            switch(wybrany) {
                case "Silnik benzynowy 1.6 (120 KM)":
                    maxObrotyTextField.setText("6000");
                    break;
                case "Silnik benzynowy 2.0 (150 KM)":
                    maxObrotyTextField.setText("6500");
                    break;
                case "Silnik diesel 2.0 (136 KM)":
                    maxObrotyTextField.setText("4500");
                    break;
                case "Silnik elektryczny (200 KM)":
                    maxObrotyTextField.setText("12000");
                    break;
            }
        }
    }

    private void onSkrzyniaSelected() {
        String wybrany = skrzyniaComboBox.getValue();
        if (wybrany != null) {
            switch(wybrany) {
                case "Skrzynia manualna 5-biegowa":
                    liczbaBiegowTextField.setText("5");
                    break;
                case "Skrzynia manualna 6-biegowa":
                    liczbaBiegowTextField.setText("6");
                    break;
                case "Skrzynia automatyczna 6-biegowa":
                    liczbaBiegowTextField.setText("6");
                    break;
                case "Skrzynia automatyczna 8-biegowa":
                    liczbaBiegowTextField.setText("8");
                    break;
            }
        }
    }

    @FXML
    private void onConfirmButton() {
        String model = modelTextField.getText().trim();
        String registration = registrationTextField.getText().trim();
        String weightText = weightTextField.getText().trim();
        String speedText = speedTextField.getText().trim();

        // Walidacja
        if (model.isEmpty() || registration.isEmpty() || weightText.isEmpty() || speedText.isEmpty()) {
            statusLabel.setText("Wszystkie pola muszą być wypełnione!");
            return;
        }

        double weight;
        int speed;

        try {
            weight = Double.parseDouble(weightText);
            if (weight <= 0) {
                statusLabel.setText("Waga musi być większa od 0!");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Waga musi być liczbą!");
            return;
        }

        try {
            speed = Integer.parseInt(speedText);
            if (speed <= 0) {
                statusLabel.setText("Prędkość musi być większa od 0!");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Prędkość musi być liczbą całkowitą!");
            return;
        }

        try {
            // Utwórz komponenty
            int maxObroty = Integer.parseInt(maxObrotyTextField.getText());
            int liczbaBiegow = Integer.parseInt(liczbaBiegowTextField.getText());

            Silnik silnik = new Silnik(silnikComboBox.getValue(), 150, 5000, maxObroty, 0);
            SkrzyniaBiegow skrzynia = new SkrzyniaBiegow(skrzyniaComboBox.getValue(), 50, 2000, 0, liczbaBiegow, liczbaBiegow);
            Pozycja pozycja = new Pozycja(400, 250);
            Sprzeglo sprzeglo = new Sprzeglo("Sprzęgło standardowe", 15, 800, false);

            // Utwórz nowy samochód
            Samochod nowySamochod = new Samochod(
                    model, registration, weight, speed,
                    silnik, skrzynia, pozycja, sprzeglo
            );

            // Przekaż nowy samochód do głównego kontrolera
            if (mainController != null) {
                mainController.dodajSamochod(nowySamochod);
            }

            // Zamknięcie okna
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            statusLabel.setText("Błąd tworzenia samochodu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelButton() {
        System.out.println("Anulowano dodawanie samochodu");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}