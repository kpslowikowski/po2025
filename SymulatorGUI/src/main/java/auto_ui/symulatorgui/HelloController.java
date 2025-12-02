package auto_ui.symulatorgui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import symulator.*;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;

public class HelloController {

    // === POLA DLA PRZYCISKÓW ===
    @FXML private Button wlaczButton;
    @FXML private Button wylaczButton;
    @FXML private Button zwiekszBiegButton;
    @FXML private Button zmniejszBiegButton;
    @FXML private Button dodajGazuButton;
    @FXML private Button ujmijGazuButton;
    @FXML private Button wcisnijSprzegloButton;
    @FXML private Button zwolnijSprzegloButton;
    @FXML private Button dodajButton;
    @FXML private Button usunButton;

    // === POLA DLA PÓL TEKSTOWYCH ===
    @FXML private TextField modelTextField;
    @FXML private TextField nrRejTextField;
    @FXML private TextField wagaTextField;
    @FXML private TextField predkoscTextField;
    @FXML private TextField pozycjaXTextField;
    @FXML private TextField pozycjaYTextField;
    @FXML private TextField biegTextField;
    @FXML private TextField maxBiegowTextField;
    @FXML private TextField obrotyTextField;
    @FXML private TextField maxObrotyTextField;
    @FXML private TextField stanSprzeglaTextField;

    // === POLA DLA INNYCH KONTROLEK ===
    @FXML private ComboBox<String> carComboBox;
    @FXML private Label statusLabel;
    @FXML private ImageView carImageView;
    @FXML private StackPane mapaPane;

    // === OBIEKTY SYMULATORA ===
    private Samochod samochod;
    private Silnik silnik;
    private SkrzyniaBiegow skrzyniaBiegow;
    private Sprzeglo sprzeglo;
    private Pozycja pozycja;
    private javafx.animation.Timeline timerSprawdzania;
    @FXML
    private ProgressBar obrotyProgressBar;


    @FXML
    public void initialize() {

        System.out.println("HelloController initialized");

        // Ładowanie ikony samochodu (punkt z instrukcji)
        try {
            Image carImage = new Image(getClass().getResource("/images/car.png").toExternalForm());
            System.out.println("Image width: " + carImage.getWidth() + ", height: " + carImage.getHeight());
            carImageView.setImage(carImage);
            carImageView.setFitWidth(30);
            carImageView.setFitHeight(20);
            carImageView.setTranslateX(0);
            carImageView.setTranslateY(0);
        } catch (Exception e) {
            System.out.println("Błąd ładowania obrazka: " + e.getMessage());
            // Jeśli nie ma obrazka, użyjemy domyślnego ustawienia
        }

        // Inicjalizacja obiektów symulatora
        initializeSymulator();

        // Inicjalizacja ComboBox
        initializeComboBox();

        // Ustawienie pozycji samochodu na środku planszy
        ustawSamochodNaSrodku();

        // Odświeżenie wszystkich pól
        refresh();

        statusLabel.setText("Symulator gotowy");
    }

    // === INICJALIZACJA SYMULATORA ===
    private void initializeSymulator() {
        try {
            // Tworzenie komponentów
            this.silnik = new Silnik("Silnik V8", 150, 5000, 6000, 0);
            this.skrzyniaBiegow = new SkrzyniaBiegow("Skrzynia manualna", 50, 2000, 0, 6, 6);
            this.pozycja = new Pozycja(0, 0);
            this.sprzeglo = new Sprzeglo("Sprzęgło hydrauliczne", 15, 800, false);
            this.samochod = new Samochod(silnik, skrzyniaBiegow, pozycja, sprzeglo);

            System.out.println("Symulator zainicjalizowany pomyślnie");
        } catch (Exception e) {
            System.out.println("Błąd inicjalizacji symulatora: " + e.getMessage());
            statusLabel.setText("Błąd inicjalizacji symulatora");
        }
    }

    // === INICJALIZACJA COMBOBOX ===
    private void initializeComboBox() {
        carComboBox.getItems().addAll(
                "Samochód 1 (Sportowy)",
                "Samochód 2 (Terenowy)",
                "Samochód 3 (Miejski)"
        );
        carComboBox.setValue("Samochód 1 (Sportowy)");

        // Obsługa zmiany wyboru w ComboBox
        carComboBox.setOnAction(event -> onCarComboBoxSelected());
    }

    // === METODA REFRESH (punkt II.2 instrukcji) ===
    private void refresh() {
        // Odświeżenie wszystkich pól z modelu
        updateSamochodFields();
        updateSkrzyniaFields();
        updateSilnikFields();
        updateSprzegloFields();
        updatePozycjaFields();
    }

    // === METODY OBSŁUGI ZDARZEŃ ===

    @FXML
    private void onWlaczButton() {
        System.out.println("Przycisk Włącz kliknięty");
        try {
            samochod.wlaczSamochod();
            refresh();
            sprawdzCzyZmienicBieg(); // DODANE
            statusLabel.setText("Silnik włączony");
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onWylaczButton() {
        System.out.println("Przycisk Wyłącz kliknięty");
        try {
            samochod.wylaczSilnik();
            refresh();
            sprawdzCzyZmienicBieg(); // DODANE
            statusLabel.setText("Silnik wyłączony");
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onZwiekszBiegButton() {
        System.out.println("Przycisk Zwiększ bieg kliknięty");
        try {
            // Sprawdź czy sprzęgło jest wciśnięte
            if (sprzeglo != null && sprzeglo.isStanSprzegla()) {
                skrzyniaBiegow.zwiekszBieg();
                refresh();
                sprawdzCzyZmienicBieg(); // DODANE
                statusLabel.setText("Bieg zwiększony na: " + skrzyniaBiegow.getAktualnyBieg());
            } else {
                statusLabel.setText("Najpierw wciśnij sprzęgło!");
                System.out.println("BŁĄD: Próba zmiany biegu bez wciśniętego sprzęgła");

                zwiekszBiegButton.setStyle("-fx-background-color: #ffcccc; -fx-border-color: red;");
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                javafx.application.Platform.runLater(() -> {
                                    zwiekszBiegButton.setStyle("");
                                });
                            }
                        },
                        1000
                );
            }
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onZmniejszBiegButton() {
        sprawdzCzyZmienicBieg();
        System.out.println("Przycisk Zmniejsz bieg kliknięty");
        try {
            // Sprawdź czy sprzęgło jest wciśnięte
            if (sprzeglo != null && sprzeglo.isStanSprzegla()) {
                skrzyniaBiegow.zmniejszBieg();
                refresh();
                statusLabel.setText("Bieg zmniejszony na: " + skrzyniaBiegow.getAktualnyBieg());
            } else {
                statusLabel.setText("Najpierw wciśnij sprzęgło!");
                System.out.println("BŁĄD: Próba zmiany biegu bez wciśniętego sprzęgła");

                // Wizualne podświetlenie błędu
                zmniejszBiegButton.setStyle("-fx-background-color: #ffcccc; -fx-border-color: red;");
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                javafx.application.Platform.runLater(() -> {
                                    zmniejszBiegButton.setStyle("");
                                });
                            }
                        },
                        1000
                );
            }
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onDodajGazuButton() {
        System.out.println("Przycisk Dodaj gazu kliknięty");
        try {
            silnik.dodajGazu();
            refresh();
            sprawdzCzyZmienicBieg(); // DODANE
            statusLabel.setText("Dodano gazu");
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onUjmijGazuButton() {
        System.out.println("Przycisk Ujmij gazu kliknięty");
        try {
            silnik.ujmijGazu();
            refresh();
            sprawdzCzyZmienicBieg(); // DODANE
            statusLabel.setText("Ujęto gazu");
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onWcisnijSprzegloButton() {
        System.out.println("Przycisk Wciśnij sprzęgło kliknięty");
        try {
            sprzeglo.wcisnij();
            refresh(); // Odświeżenie po akcji
            statusLabel.setText("Sprzęgło wciśnięte");
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onZwolnijSprzegloButton() {
        System.out.println("Przycisk Zwolnij sprzęgło kliknięty");
        try {
            sprzeglo.zwolnij();
            refresh(); // Odświeżenie po akcji
            statusLabel.setText("Sprzęgło zwolnione");
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }



    @FXML
    private void onDodajButton() {
        System.out.println("Przycisk Dodaj nowy kliknięty");

        // TERAZ UŻYJ openAddCarWindow()
        openAddCarWindow();
    }

    @FXML
    private void onUsunButton() {
        System.out.println("Przycisk Usuń kliknięty");
        try {
            String wybrany = carComboBox.getValue();
            if (wybrany != null && !wybrany.isEmpty()) {
                carComboBox.getItems().remove(wybrany);
                statusLabel.setText("Usunięto samochód: " + wybrany);
                refreshCarComboBox();
                refresh();
            }
        } catch (Exception e) {
            statusLabel.setText("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onCarComboBoxSelected() {
        String wybrany = carComboBox.getValue();
        System.out.println("Wybrano samochód: " + wybrany);

        if (wybrany == null || wybrany.isEmpty()) {
            return;
        }

        // Sprawdź czy to jest nowy samochód z formularza
        List<DodajSamochodController.NowySamochodData> noweDane =
                DodajSamochodController.getDodaneSamochody();

        for (DodajSamochodController.NowySamochodData dane : noweDane) {
            String nazwaZFormularza = dane.model + " (" + dane.nrRej + ")";

            if (nazwaZFormularza.equals(wybrany)) {
                // To jest nowy samochód z formularza
                utworzIZaktualizujSamochod(dane);
                refresh();
                statusLabel.setText("Wybrano: " + dane.model);
                return;
            }
        }

        // Jeśli nie znaleziono w nowych danych, to użyj domyślnego
        statusLabel.setText("Wybrano: " + wybrany);
        refresh(); // Odświeżenie po zmianie samochodu
    }

    // === METODY POMOCNICZE ===

    private void updateSamochodFields() {
        if (samochod != null) {
            modelTextField.setText(samochod.getModel());
            nrRejTextField.setText(samochod.getNrRej());
            wagaTextField.setText(String.valueOf(samochod.getWaga()) + " kg");
            predkoscTextField.setText(samochod.getPredkosc() + " km/h");
        }
    }

    private void updateSkrzyniaFields() {
        if (skrzyniaBiegow != null) {
            biegTextField.setText(String.valueOf(skrzyniaBiegow.getAktualnyBieg()));
            maxBiegowTextField.setText(String.valueOf(skrzyniaBiegow.getIloscBiegow()));
        }
    }

    private void updateSilnikFields() {
        if (silnik != null) {
            obrotyTextField.setText(silnik.getObroty() + " RPM");
            maxObrotyTextField.setText(silnik.getMaxObroty() + " RPM");

            // Aktualizuj ProgressBar
            if (obrotyProgressBar != null) {
                double progress = (double) silnik.getObroty() / silnik.getMaxObroty();
                obrotyProgressBar.setProgress(progress);

                // Kolor w zależności od obrotów
                if (progress > 0.9) {
                    obrotyProgressBar.setStyle("-fx-accent: red;");
                } else if (progress > 0.75) {
                    obrotyProgressBar.setStyle("-fx-accent: orange;");
                } else {
                    obrotyProgressBar.setStyle("-fx-accent: green;");
                }
            }
        }
    }

    private void updateSprzegloFields() {
        if (sprzeglo != null) {
            stanSprzeglaTextField.setText(sprzeglo.isStanSprzegla() ? "WCIŚNIĘTE" : "ZWOLNIONE");
        }
    }

    private void updatePozycjaFields() {
        if (pozycja != null) {
            pozycjaXTextField.setText(String.format("%.1f", pozycja.getX()));
            pozycjaYTextField.setText(String.format("%.1f", pozycja.getY()));
        }
    }

    private void ustawSamochodNaSrodku() {
        // Ustawienie samochodu na środku planszy
        double centerX = 400;
        double centerY = 250;

        if (pozycja != null) {
            pozycja.UaktualnijPozycje(centerX, centerY);
        }

        // Aktualizacja pozycji obrazka
        if (carImageView != null) {
            carImageView.setTranslateX(centerX - 40);
            carImageView.setTranslateY(centerY - 20);
        }
    }

    // === METODA DO OTWIERANIA OKNA DODAWANIA SAMOCHODU ===
    private void openAddCarWindow() {
        try {
            System.out.println("Otwieranie okna dodawania samochodu...");

            // Załaduj formularz FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DodajSamochod.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dodaj nowy samochód");
            stage.setResizable(false);

            // Ustaw jako okno modalne
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pokaż okno i czekaj na zamknięcie
            stage.showAndWait();

            // Po zamknięciu okna, odśwież listę
            refreshCarComboBox();

            // Po dodaniu nowego auta, odśwież dane
            odswiezDanePoDodaniu();

            statusLabel.setText("Dodano nowy samochód - wybierz z listy");

        } catch (Exception e) {
            System.out.println("Błąd otwierania formularza: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Błąd: Nie można otworzyć formularza");
        }
    }

    // === METODA DO ODSWIEŻANIA COMBOBOX Z SAMOCHODAMI ===
    // === METODA DO ODSWIEŻANIA COMBOBOX Z SAMOCHODAMI ===
    private void refreshCarComboBox() {
        try {
            // Pobierz aktualną listę samochodów z DodajSamochodController
            // UŻYJ POPRAWNEJ NAZWY METODY:
            List<DodajSamochodController.NowySamochodData> noweDane =
                    DodajSamochodController.getDodaneSamochody();

            if (noweDane == null || noweDane.isEmpty()) {
                return; // Nie ma nowych samochodów
            }

            // Zachowaj aktualny wybór
            String aktualnyWybór = carComboBox.getValue();

            // Dodaj nowe samochody które jeszcze nie są na liście
            for (DodajSamochodController.NowySamochodData dane : noweDane) {
                String nazwaSamochodu = dane.model + " (" + dane.nrRej + ")";

                if (!carComboBox.getItems().contains(nazwaSamochodu)) {
                    carComboBox.getItems().add(nazwaSamochodu);
                    System.out.println("Dodano do ComboBox: " + nazwaSamochodu);
                }
            }

            // Jeśli nic nie było wybrane, wybierz pierwszy
            if (aktualnyWybór == null && !carComboBox.getItems().isEmpty()) {
                carComboBox.setValue(carComboBox.getItems().get(0));
            }

        } catch (Exception e) {
            System.out.println("Błąd odświeżania ComboBox: " + e.getMessage());
        }
    }
    private void odswiezDanePoDodaniu() {
        // Pobierz ostatnio dodany samochód
        List<DodajSamochodController.NowySamochodData> noweDane =
                DodajSamochodController.getDodaneSamochody();

        if (noweDane.isEmpty()) {
            return;
        }

        // Weź ostatni dodany samochód
        DodajSamochodController.NowySamochodData ostatni = noweDane.get(noweDane.size() - 1);
        String nazwaNowego = ostatni.model + " (" + ostatni.nrRej + ")";

        // Jeśli to jest wybrane w ComboBox, zaktualizuj dane
        if (carComboBox.getValue() != null && carComboBox.getValue().equals(nazwaNowego)) {
            // Tworzymy nowy obiekt samochodu z danych formularza
            utworzIZaktualizujSamochod(ostatni);

            // Odśwież widok
            refresh();
            statusLabel.setText("Wybrano nowy samochód: " + ostatni.model);
        }
    }
    // === UTWÓRZ I ZAKTUALIZUJ SAMOCHOD Z DANYCH ===
    private void utworzIZaktualizujSamochod(DodajSamochodController.NowySamochodData dane) {
        try {
            // Utwórz komponenty na podstawie danych
            int maxObroty = 6000;
            if (dane.typSilnika.contains("2.0 (150 KM)")) maxObroty = 6500;
            if (dane.typSilnika.contains("diesel")) maxObroty = 4500;
            if (dane.typSilnika.contains("elektryczny")) maxObroty = 12000;

            int liczbaBiegow = 5;
            if (dane.typSkrzyni.contains("6-biegowa")) liczbaBiegow = 6;
            if (dane.typSkrzyni.contains("8-biegowa")) liczbaBiegow = 8;

            // Utwórz nowe komponenty
            this.silnik = new Silnik(dane.typSilnika, 150, 5000, maxObroty, 0);
            this.skrzyniaBiegow = new SkrzyniaBiegow(dane.typSkrzyni, 50, 2000, 0, liczbaBiegow, liczbaBiegow);
            this.pozycja = new Pozycja(0, 0);
            this.sprzeglo = new Sprzeglo("Sprzęgło standardowe", 15, 800, false);

            // Utwórz nowy samochód
            this.samochod = new Samochod(
                    dane.model,
                    dane.nrRej,
                    dane.waga,
                    dane.predkoscMax,
                    silnik,
                    skrzyniaBiegow,
                    pozycja,
                    sprzeglo
            );

            System.out.println("Utworzono nowy samochód: " + dane.model);

        } catch (Exception e) {
            System.out.println("Błąd tworzenia samochodu: " + e.getMessage());
        }
    }
    // === SPRAWDZANIE CZY TRZEBA ZMIENIĆ BIEG ===
    private void sprawdzCzyZmienicBieg() {
        if (silnik == null || skrzyniaBiegow == null) {
            return;
        }

        int obroty = silnik.getObroty();
        int aktualnyBieg = skrzyniaBiegow.getAktualnyBieg();
        int maxObroty = silnik.getMaxObroty();

        // Definiujemy progi dla zmiany biegu
        int progZmianyWGore = (int)(maxObroty * 0.85); // 85% maksymalnych obrotów
        int progZmianyWDol = (int)(maxObroty * 0.40); // 40% maksymalnych obrotów

        // Sprawdź czy obroty są za wysokie dla aktualnego biegu
        if (obroty > progZmianyWGore && aktualnyBieg < skrzyniaBiegow.getIloscBiegow()) {
            // Za wysokie obroty - podświetl przycisk zwiększania biegu
            zwiekszBiegButton.setStyle("-fx-background-color: #ffcc00; -fx-border-color: orange; -fx-font-weight: bold;");
            statusLabel.setText("UWAGA: Obroty za wysokie! Zwiększ bieg.");

            // Możesz też dodać dźwięk/wibrację (opcjonalnie)
            System.out.println("ALERT: Obroty za wysokie! (" + obroty + " RPM)");

        } else if (obroty < progZmianyWDol && aktualnyBieg > 0) {
            // Za niskie obroty - podświetl przycisk zmniejszania biegu
            zmniejszBiegButton.setStyle("-fx-background-color: #ffcc00; -fx-border-color: orange; -fx-font-weight: bold;");
            statusLabel.setText("UWAGA: Obroty za niskie! Zmniejsz bieg.");

            System.out.println("ALERT: Obroty za niskie! (" + obroty + " RPM)");

        } else {
            // Normalny zakres - przywróć domyślny styl
            zwiekszBiegButton.setStyle("");
            zmniejszBiegButton.setStyle("");
        }

        // Podświetl pole obrotów jeśli są w niebezpiecznym zakresie
        if (obroty > (int)(maxObroty * 0.90)) { // Powyżej 90%
            obrotyTextField.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white; -fx-font-weight: bold;");
        } else if (obroty > (int)(maxObroty * 0.75)) { // Powyżej 75%
            obrotyTextField.setStyle("-fx-background-color: #ffffcc; -fx-font-weight: bold;");
        } else {
            obrotyTextField.setStyle("");
        }
    }


}