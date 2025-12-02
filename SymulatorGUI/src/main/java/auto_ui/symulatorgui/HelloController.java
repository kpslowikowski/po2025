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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HelloController implements symulator.Listener {

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
    @FXML private ComboBox<Samochod> carComboBox;
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

    // Lista samochodów
    private ObservableList<Samochod> samochody = FXCollections.observableArrayList();


    @Override
    public void update() {
        // Wywoływane przez Samochod gdy zmieni się jego stan
        Platform.runLater(() -> {
            refresh();
            updateCarImagePosition();
        });
    }

    @FXML
    public void initialize() {
        System.out.println("HelloController initialized");

        // Ładowanie ikony samochodu
        try {
            Image carImage = new Image(getClass().getResource("/images/car.png").toExternalForm());
            carImageView.setImage(carImage);
            carImageView.setFitWidth(60);
            carImageView.setFitHeight(30);
        } catch (Exception e) {
            System.out.println("Błąd ładowania obrazka: " + e.getMessage());
            // Utwórz prosty prostokąt jako fallback
        }

        // Inicjalizacja obiektów symulatora
        initializeSymulator();

        // Inicjalizacja ComboBox
        initializeComboBox();

        // Ustawienie pozycji samochodu na środku planszy
        ustawSamochodNaSrodku();

        // Odświeżenie wszystkich pól
        refresh();

        // Obsługa kliknięć myszą na mapie
        setupMouseClickHandler();

        statusLabel.setText("Symulator gotowy");
    }

    // === INICJALIZACJA SYMULATORA ===
    private void initializeSymulator() {
        try {
            // Tworzenie komponentów
            this.silnik = new Silnik("Silnik V8", 150, 5000, 6000, 0);
            this.skrzyniaBiegow = new SkrzyniaBiegow("Skrzynia manualna", 50, 2000, 0, 6, 6);
            this.pozycja = new Pozycja(400, 250);
            this.sprzeglo = new Sprzeglo("Sprzęgło hydrauliczne", 15, 800, false);
            this.samochod = new Samochod(silnik, skrzyniaBiegow, pozycja, sprzeglo);

            // Dodaj kontroler jako listener do samochodu
            this.samochod.addListener(this);

            // Dodaj samochód do listy
            samochody.add(samochod);

            System.out.println("Symulator zainicjalizowany pomyślnie");
            System.out.println("Pozycja startowa: " + pozycja.getX() + ", " + pozycja.getY());
        } catch (Exception e) {
            System.out.println("Błąd inicjalizacji symulatora: " + e.getMessage());
            statusLabel.setText("Błąd inicjalizacji symulatora");
        }
    }

    // === INICJALIZACJA COMBOBOX ===
    private void initializeComboBox() {
        carComboBox.setItems(samochody);

        // Ustaw konwerter do wyświetlania nazw samochodów
        carComboBox.setCellFactory(param -> new ListCell<Samochod>() {
            @Override
            protected void updateItem(Samochod item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getModel() + " (" + item.getNrRej() + ")");
                }
            }
        });

        carComboBox.setButtonCell(new ListCell<Samochod>() {
            @Override
            protected void updateItem(Samochod item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getModel() + " (" + item.getNrRej() + ")");
                }
            }
        });

        // Ustaw pierwszy samochód jako wybrany
        if (!samochody.isEmpty()) {
            carComboBox.setValue(samochody.get(0));
        }

        // Obsługa zmiany wyboru w ComboBox
        carComboBox.setOnAction(event -> onCarComboBoxSelected());
    }

    private void setupMouseClickHandler() {
        mapaPane.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            System.out.println("Kliknięto na mapie: X=" + x + ", Y=" + y);
            System.out.println("Aktualna pozycja auta: X=" + samochod.getPozycja().getX() + ", Y=" + samochod.getPozycja().getY());

            Pozycja nowaPozycja = new Pozycja(x, y);

            if (samochod != null) {
                samochod.jedzDo(nowaPozycja);
                statusLabel.setText("Jadę do pozycji: (" + String.format("%.0f", x) + ", " + String.format("%.0f", y) + ")");
            }
        });
    }

    // === METODA REFRESH ===
    private void refresh() {
        // Odświeżenie wszystkich pól z modelu
        updateSamochodFields();
        updateSkrzyniaFields();
        updateSilnikFields();
        updateSprzegloFields();
        updatePozycjaFields();
        updateCarImagePosition();
    }

    private void updateCarImagePosition() {
        if (samochod != null && carImageView != null && samochod.getPozycja() != null) {
            Platform.runLater(() -> {
                try {
                    double x = samochod.getPozycja().getX();
                    double y = samochod.getPozycja().getY();

                    // Debug: wypisz pozycję
                    if (Math.random() < 0.1) { // Tylko czasami, żeby nie zaśmiecać konsoli
                        System.out.println("Aktualizuję pozycję obrazka: X=" + x + ", Y=" + y);
                    }

                    // Ustaw pozycję obrazka
                    carImageView.setTranslateX(x - carImageView.getFitWidth() / 2);
                    carImageView.setTranslateY(y - carImageView.getFitHeight() / 2);

                } catch (Exception e) {
                    System.out.println("Błąd aktualizacji obrazka: " + e.getMessage());
                }
            });
        }
    }

    // === METODY OBSŁUGI ZDARZEŃ ===

    @FXML
    private void onWlaczButton() {
        System.out.println("Przycisk Włącz kliknięty");
        try {
            samochod.wlaczSamochod();
            refresh();
            sprawdzCzyZmienicBieg();
            statusLabel.setText("Silnik włączony");
        } catch (Exception e) {
            pokazBlad("Błąd przy włączaniu silnika: " + e.getMessage());
        }
    }

    @FXML
    private void onWylaczButton() {
        System.out.println("Przycisk Wyłącz kliknięty");
        try {
            samochod.wylaczSilnik();
            refresh();
            sprawdzCzyZmienicBieg();
            statusLabel.setText("Silnik wyłączony");
        } catch (Exception e) {
            pokazBlad("Błąd przy wyłączaniu silnika: " + e.getMessage());
        }
    }

    @FXML
    private void onZwiekszBiegButton() {
        System.out.println("Przycisk Zwiększ bieg kliknięty");
        try {
            if (sprzeglo != null && sprzeglo.isStanSprzegla()) {
                skrzyniaBiegow.zwiekszBieg();
                refresh();
                sprawdzCzyZmienicBieg();
                statusLabel.setText("Bieg zwiększony na: " + skrzyniaBiegow.getAktualnyBieg());
            } else {
                pokazBlad("Najpierw wciśnij sprzęgło!");
                zwiekszBiegButton.setStyle("-fx-background-color: #ffcccc; -fx-border-color: red;");
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    zwiekszBiegButton.setStyle("");
                                });
                            }
                        },
                        1000
                );
            }
        } catch (Exception e) {
            pokazBlad("Błąd przy zmianie biegu: " + e.getMessage());
        }
    }

    @FXML
    private void onZmniejszBiegButton() {
        System.out.println("Przycisk Zmniejsz bieg kliknięty");
        try {
            if (sprzeglo != null && sprzeglo.isStanSprzegla()) {
                skrzyniaBiegow.zmniejszBieg();
                refresh();
                sprawdzCzyZmienicBieg();
                statusLabel.setText("Bieg zmniejszony na: " + skrzyniaBiegow.getAktualnyBieg());
            } else {
                pokazBlad("Najpierw wciśnij sprzęgło!");
                zmniejszBiegButton.setStyle("-fx-background-color: #ffcccc; -fx-border-color: red;");
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    zmniejszBiegButton.setStyle("");
                                });
                            }
                        },
                        1000
                );
            }
        } catch (Exception e) {
            pokazBlad("Błąd przy zmianie biegu: " + e.getMessage());
        }
    }

    @FXML
    private void onDodajGazuButton() {
        System.out.println("Przycisk Dodaj gazu kliknięty");
        try {
            silnik.dodajGazu();
            refresh();
            sprawdzCzyZmienicBieg();
            statusLabel.setText("Dodano gazu");
        } catch (Exception e) {
            pokazBlad("Błąd przy dodawaniu gazu: " + e.getMessage());
        }
    }

    @FXML
    private void onUjmijGazuButton() {
        System.out.println("Przycisk Ujmij gazu kliknięty");
        try {
            silnik.ujmijGazu();
            refresh();
            sprawdzCzyZmienicBieg();
            statusLabel.setText("Ujęto gazu");
        } catch (Exception e) {
            pokazBlad("Błąd przy ujmowaniu gazu: " + e.getMessage());
        }
    }

    @FXML
    private void onWcisnijSprzegloButton() {
        System.out.println("Przycisk Wciśnij sprzęgło kliknięty");
        try {
            sprzeglo.wcisnij();
            refresh();
            statusLabel.setText("Sprzęgło wciśnięte");
        } catch (Exception e) {
            pokazBlad("Błąd przy wciskaniu sprzęgła: " + e.getMessage());
        }
    }

    @FXML
    private void onZwolnijSprzegloButton() {
        System.out.println("Przycisk Zwolnij sprzęgło kliknięty");
        try {
            sprzeglo.zwolnij();
            refresh();
            statusLabel.setText("Sprzęgło zwolnione");
        } catch (Exception e) {
            pokazBlad("Błąd przy zwalnianiu sprzęgła: " + e.getMessage());
        }
    }

    @FXML
    private void onDodajButton() {
        System.out.println("Przycisk Dodaj nowy kliknięty");
        openAddCarWindow();
    }

    @FXML
    private void onUsunButton() {
        System.out.println("Przycisk Usuń kliknięty");
        try {
            Samochod wybrany = carComboBox.getValue();
            if (wybrany != null) {
                samochody.remove(wybrany);
                if (!samochody.isEmpty()) {
                    carComboBox.setValue(samochody.get(0));
                } else {
                    carComboBox.setValue(null);
                }
                statusLabel.setText("Usunięto samochód: " + wybrany.getModel());
                refresh();
            }
        } catch (Exception e) {
            pokazBlad("Błąd przy usuwaniu samochodu: " + e.getMessage());
        }
    }

    @FXML
    private void onCarComboBoxSelected() {
        Samochod wybrany = carComboBox.getValue();
        System.out.println("Wybrano samochód: " + (wybrany != null ? wybrany.getModel() : "null"));

        if (wybrany == null) {
            return;
        }

        // Zaktualizuj referencje
        this.samochod = wybrany;
        this.silnik = wybrany.getSilnik();
        this.skrzyniaBiegow = wybrany.getSkrzyniaBiegow();
        this.sprzeglo = wybrany.getSprzeglo();
        this.pozycja = wybrany.getPozycja();

        // Dodaj kontroler jako listener do nowego samochodu
        wybrany.addListener(this);

        refresh();
        updateCarImagePosition();
        statusLabel.setText("Wybrano: " + wybrany.getModel());
    }

    // === METODY POMOCNICZE ===

    private void updateSamochodFields() {
        if (samochod != null) {
            modelTextField.setText(samochod.getModel());
            nrRejTextField.setText(samochod.getNrRej());
            wagaTextField.setText(String.format("%.1f kg", samochod.getWaga()));
            predkoscTextField.setText(String.format("%.1f km/h", samochod.getPredkosc()));
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

            if (obrotyProgressBar != null) {
                double progress = (double) silnik.getObroty() / silnik.getMaxObroty();
                obrotyProgressBar.setProgress(progress);

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
        double centerX = 400;
        double centerY = 250;

        if (pozycja != null) {
            pozycja.UaktualnijPozycje(centerX, centerY);
        }

        updateCarImagePosition();
    }

    private void openAddCarWindow() {
        try {
            System.out.println("Otwieranie okna dodawania samochodu...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("DodajSamochod.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dodaj nowy samochód");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pobierz kontroler formularza i przekaż referencję do głównego kontrolera
            DodajSamochodController dodajController = loader.getController();
            dodajController.setMainController(this);

            stage.showAndWait();

        } catch (Exception e) {
            System.out.println("Błąd otwierania formularza: " + e.getMessage());
            e.printStackTrace();
            pokazBlad("Nie można otworzyć formularza: " + e.getMessage());
        }
    }

    public void dodajSamochod(Samochod nowySamochod) {
        samochody.add(nowySamochod);
        carComboBox.setValue(nowySamochod);

        nowySamochod.addListener(this);

        statusLabel.setText("Dodano nowy samochód: " + nowySamochod.getModel());
    }

    // === SPRAWDZANIE CZY TRZEBA ZMIENIĆ BIEG ===
    private void sprawdzCzyZmienicBieg() {
        if (silnik == null || skrzyniaBiegow == null) {
            return;
        }

        int obroty = silnik.getObroty();
        int aktualnyBieg = skrzyniaBiegow.getAktualnyBieg();
        int maxObroty = silnik.getMaxObroty();

        int progZmianyWGore = (int)(maxObroty * 0.85);
        int progZmianyWDol = (int)(maxObroty * 0.40);

        if (obroty > progZmianyWGore && aktualnyBieg < skrzyniaBiegow.getIloscBiegow()) {
            zwiekszBiegButton.setStyle("-fx-background-color: #ffcc00; -fx-border-color: orange; -fx-font-weight: bold;");
            statusLabel.setText("UWAGA: Obroty za wysokie! Zwiększ bieg.");
        } else if (obroty < progZmianyWDol && aktualnyBieg > 0) {
            zmniejszBiegButton.setStyle("-fx-background-color: #ffcc00; -fx-border-color: orange; -fx-font-weight: bold;");
            statusLabel.setText("UWAGA: Obroty za niskie! Zmniejsz bieg.");
        } else {
            zwiekszBiegButton.setStyle("");
            zmniejszBiegButton.setStyle("");
        }

        if (obroty > (int)(maxObroty * 0.90)) {
            obrotyTextField.setStyle("-fx-background-color: #ff6666; -fx-text-fill: white; -fx-font-weight: bold;");
        } else if (obroty > (int)(maxObroty * 0.75)) {
            obrotyTextField.setStyle("-fx-background-color: #ffffcc; -fx-font-weight: bold;");
        } else {
            obrotyTextField.setStyle("");
        }
    }

    // === OBSŁUGA BŁĘDÓW ===
    public void pokazBlad(String wiadomosc) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(null);
            alert.setContentText(wiadomosc);
            alert.showAndWait();
        });
    }
}