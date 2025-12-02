package auto_ui.symulatorgui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

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

    // Klasa do przechowywania danych samochodu
    public static class NowySamochodData {
        public String model;
        public String nrRej;
        public double waga;
        public int predkoscMax;
        public String typSilnika;
        public String typSkrzyni;

        public NowySamochodData(String model, String nrRej, double waga, int predkoscMax,
                                String typSilnika, String typSkrzyni) {
            this.model = model;
            this.nrRej = nrRej;
            this.waga = waga;
            this.predkoscMax = predkoscMax;
            this.typSilnika = typSilnika;
            this.typSkrzyni = typSkrzyni;
        }

        @Override
        public String toString() {
            return model + " (" + nrRej + ")";
        }
    }

    // Lista dodanych samochodów (dla wszystkich instancji)
    private static List<NowySamochodData> dodaneSamochody = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("DodajSamochodController initialized");

        // Inicjalizacja ComboBox
        initializeSilnikComboBox();
        initializeSkrzyniaComboBox();

        // Obsługa zmian
        silnikComboBox.setOnAction(event -> onSilnikSelected());
        skrzyniaComboBox.setOnAction(event -> onSkrzyniaSelected());

        // Wywołaj raz na starcie
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

        // Utwórz obiekt z danymi
        NowySamochodData nowySamochod = new NowySamochodData(
                model,
                registration,
                weight,
                speed,
                silnikComboBox.getValue(),
                skrzyniaComboBox.getValue()
        );

        // Dodaj do listy
        dodaneSamochody.add(nowySamochod);

        System.out.println("Dodano nowy samochód: " + nowySamochod);
        System.out.println("  Waga: " + weight + " kg, Prędkość max: " + speed + " km/h");
        System.out.println("  Silnik: " + nowySamochod.typSilnika);
        System.out.println("  Skrzynia: " + nowySamochod.typSkrzyni);

        // Zamknięcie okna
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButton() {
        System.out.println("Anulowano dodawanie samochodu");
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // === METODY PUBLICZNE DLA INTEGRACJI ===

    public static List<NowySamochodData> getDodaneSamochody() {
        return new ArrayList<>(dodaneSamochody); // Zwróć kopię
    }

    public static void clearDodaneSamochody() {
        dodaneSamochody.clear();
    }

    public static void addCarToList(String model, String registration, double weight, int speed) {
        NowySamochodData samochod = new NowySamochodData(
                model, registration, weight, speed, "Standardowy", "Manualna"
        );
        dodaneSamochody.add(samochod);
    }
}