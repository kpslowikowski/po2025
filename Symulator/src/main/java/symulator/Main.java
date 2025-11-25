package symulator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {

    private Silnik silnik;
    private SkrzyniaBiegow skrzyniaBiegow;
    private Pozycja pozycja;
    private Samochod samochod;
    private Sprzeglo sprzeglo;

    // === ZMIENIŁEM: Rectangle na ImageView ===
    private StackPane centerSection;
    private Pane plansza;
    private ImageView samochodGrafika; // TERAZ TO ImageView!
    private Text pozycjaText;
    private Text wymiaryText;

    // === STAŁE WYMIARY PLANSZY ===
    private static final int PLANSZA_SZEROKOSC = 600;
    private static final int PLANSZA_WYSOKOSC = 400;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // === INICJALIZACJA TWOJEJ LOGIKI ===
        initializeCarLogic();

        // === BUDOWANIE GUI ===
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setTop(createTopSection());
        root.setLeft(createLeftSection());
        root.setCenter(createCenterSection()); // PLANSZA Z SAMOCHODEM
        root.setBottom(createBottomSection());

        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulator Samochodu");
        primaryStage.show();

        // === POCZĄTKOWA POZYCJA SAMOCHODU NA ŚRODKU ===
        ustawSamochodNaSrodku();
    }

    private void initializeCarLogic() {
        this.silnik = new Silnik("V8", 50, 100, 30, 30);
        this.skrzyniaBiegow = new SkrzyniaBiegow("Manualna", 100, 100, 1, 10, 10);
        this.pozycja = new Pozycja(0, 0);
        this.sprzeglo = new Sprzeglo("Hydrauliczne", 20, 50, false);
        this.samochod = new Samochod(silnik, skrzyniaBiegow, pozycja);

        System.out.println("Logika samochodu zainicjalizowana!");
    }

    // === SEKCJA CENTER - PLANSZA Z SAMOCHODEM ===
    private StackPane createCenterSection() {
        centerSection = new StackPane();
        centerSection.setPadding(new Insets(10));
        centerSection.setStyle("-fx-border-color: #333333; -fx-border-width: 2px;");

        // === TWORZENIE PLANSZY ===
        plansza = new Pane();
        plansza.setPrefSize(PLANSZA_SZEROKOSC, PLANSZA_WYSOKOSC);
        plansza.setStyle("-fx-background-color: #90EE90; -fx-border-color: #2E8B57; -fx-border-width: 3px;");

        // === ZMIENIŁEM: Tworzenie samochodu z obrazka ===
        samochodGrafika = createSamochodZObrazka();

        // === TEKST Z POZYCJĄ ===
        pozycjaText = new Text();
        pozycjaText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-fill: #333333;");

        // === TEKST Z WYMIARAMI PLANSZY ===
        wymiaryText = new Text("Plansza: " + PLANSZA_SZEROKOSC + " x " + PLANSZA_WYSOKOSC + "\n" +
                "Użyj: pozycja.UaktualnijPozycje(x, y)\n" +
                "Zakres: x: 0-" + PLANSZA_SZEROKOSC + ", y: 0-" + PLANSZA_WYSOKOSC);
        wymiaryText.setStyle("-fx-font-size: 12px; -fx-fill: #555555;");
        wymiaryText.setX(10);
        wymiaryText.setY(PLANSZA_WYSOKOSC - 40);

        // === DODAJEMY WSZYSTKO DO PLANSZY ===
        plansza.getChildren().addAll(samochodGrafika, pozycjaText, wymiaryText);
        centerSection.getChildren().add(plansza);

        return centerSection;
    }

    // === NOWA METODA: ŁADUJE OBRAZEK SAMOCHODU ===
    // === METODA ŁADUJE OBRAZEK SAMOCHODU - WERSJA Z BEZWZGLĘDNĄ ŚCIEŻKĄ ===
    private ImageView createSamochodZObrazka() {
        try {
            // OPCJA 1: Spróbuj z resources (działa jeśli folder jest oznaczony)
            InputStream inputStream = getClass().getResourceAsStream("/samochod.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(80);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                System.out.println("✅ Załadowano grafikę samochodu z resources!");
                return imageView;
            }

            // OPCJA 2: Spróbuj z bezwzględnej ścieżki
            String[] sciezki = {
                    "src/resources/samochod.png",                    // Relatywna ścieżka
                    "Symulator/src/resources/samochod.png",          // Z nazwą projektu
                    System.getProperty("user.dir") + "/src/resources/samochod.png" // Bezwzględna
            };

            for (String sciezka : sciezki) {
                try {
                    Image image = new Image("file:" + sciezka);
                    if (!image.isError()) {
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(80);
                        imageView.setFitHeight(40);
                        imageView.setPreserveRatio(true);
                        System.out.println("✅ Załadowano grafikę z: " + sciezka);
                        return imageView;
                    }
                } catch (Exception e) {
                    // Kontynuuj do następnej ścieżki
                }
            }

            System.out.println("❌ Nie znaleziono samochod.png w żadnej lokalizacji");

        } catch (Exception e) {
            System.out.println("❌ Błąd ładowania obrazka: " + e.getMessage());
        }

        // OPCJA 3: Zastępczy ładny samochód z kształtów
        System.out.println("⚠️ Używam zastępczej grafiki");
        return createLadnySamochod();
    }

    // === METODA TWORZĄCA ZASTĘPCZEGO SAMOCHODA ===
    private ImageView createLadnySamochod() {
        // Tworzymy ładnego samochoda z kształtów
        Pane carPane = new Pane();
        carPane.setPrefSize(80, 40);

        // KAROSERIA (niebieska z zaokrąglonymi rogami)
        Rectangle karoseria = new Rectangle(70, 30, Color.DODGERBLUE);
        karoseria.setArcWidth(15);
        karoseria.setArcHeight(15);
        karoseria.setX(5);
        karoseria.setY(5);
        karoseria.setStroke(Color.DARKBLUE);
        karoseria.setStrokeWidth(2);

        // DACH (ciemniejszy niebieski)
        Rectangle dach = new Rectangle(50, 15, Color.ROYALBLUE);
        dach.setArcWidth(10);
        dach.setArcHeight(10);
        dach.setX(15);
        dach.setY(0);

        // KOŁA (czarne)
        Rectangle koloPrzednie = new Rectangle(10, 10, Color.BLACK);
        koloPrzednie.setArcWidth(10);
        koloPrzednie.setArcHeight(10);
        koloPrzednie.setX(10);
        koloPrzednie.setY(25);

        Rectangle koloTylne = new Rectangle(10, 10, Color.BLACK);
        koloTylne.setArcWidth(10);
        koloTylne.setArcHeight(10);
        koloTylne.setX(60);
        koloTylne.setY(25);

        // ŚWIATŁA
        Rectangle swiatloPrzednie = new Rectangle(6, 4, Color.YELLOW);
        swiatloPrzednie.setX(70);
        swiatloPrzednie.setY(10);

        Rectangle swiatloTylne = new Rectangle(6, 4, Color.RED);
        swiatloTylne.setX(0);
        swiatloTylne.setY(10);

        carPane.getChildren().addAll(karoseria, dach, koloPrzednie, koloTylne, swiatloPrzednie, swiatloTylne);

        // Konwersja Pane na ImageView
        ImageView imageView = new ImageView();
        // W prawdziwym projekcie użyj snapshot(), ale na razie zostawiamy kształty
        return imageView;
    }

    // === METODA USTAWIAJĄCA SAMOCHÓD NA ŚRODKU ===
    private void ustawSamochodNaSrodku() {
        double srodekX = PLANSZA_SZEROKOSC / 2 - 40; // 40 = połowa szerokości samochodu
        double srodekY = PLANSZA_WYSOKOSC / 2 - 20;  // 20 = połowa wysokości samochodu

        // AKTUALIZUJ POZYCJĘ W TWOJEJ KLASIE
        pozycja.UaktualnijPozycje(srodekX, srodekY);

        // AKTUALIZUJ GRAFIKĘ
        aktualizujGrafikeSamochodu();
    }

    // === METODA AKTUALIZUJĄCA GRAFIKĘ SAMOCHODU ===
    private void aktualizujGrafikeSamochodu() {
        // USTAW POZYCJĘ GRAFIKI SAMOCHODU
        samochodGrafika.setX(pozycja.x);
        samochodGrafika.setY(pozycja.y);

        // AKTUALIZUJ TEKST Z POZYCJĄ
        pozycjaText.setText(String.format("Samochód: (%.1f, %.1f)", pozycja.x, pozycja.y));
        pozycjaText.setX(pozycja.x + 85); // WIĘCEJ MIEJSCA OBOK SAMOCHODU
        pozycjaText.setY(pozycja.y + 20);

        System.out.println("Pozycja samochodu: " + pozycja.x + ", " + pozycja.y);
    }

    // === METODA DO TESTOWANIA - PRZENOSZENIE SAMOCHODU ===
    private void testPrzeniesSamochod() {
        // PRZYKŁAD: PRZENIES SAMOCHÓD W RÓŻNE MIEJSCA
        System.out.println("=== TEST PRZENOSZENIA SAMOCHODU ===");

        // PRZYKŁAD 1: LEWY GÓRNY RÓG
        pozycja.UaktualnijPozycje(50, 50);
        aktualizujGrafikeSamochodu();

        // PRZYKŁAD 2: PRAWY DOLNY RÓG
        pozycja.UaktualnijPozycje(PLANSZA_SZEROKOSC - 90, PLANSZA_WYSOKOSC - 50);
        aktualizujGrafikeSamochodu();

        // PRZYKŁAD 3: Z POWROTEM NA ŚRODEK
        ustawSamochodNaSrodku();
    }

    // === POZOSTAŁE METODY (BEZ ZMIAN) ===
    private MenuBar createTopSection() {
        MenuBar menuBar = new MenuBar();
        Menu plikMenu = new Menu("Plik");
        Menu edycjaMenu = new Menu("Edycja");
        menuBar.getMenus().addAll(plikMenu, edycjaMenu);
        return menuBar;
    }

    private HBox createBottomSection() {
        HBox bottomSection = new HBox(10);
        bottomSection.setPadding(new Insets(10));
        bottomSection.setAlignment(Pos.CENTER);

        Button dodajButton = new Button("Dodaj nowy");
        Button usunButton = new Button("Usuń");

        // === DODAJEMY PRZYCISK DO TESTOWANIA POZYCJI ===
        Button testPozycjaButton = new Button("Test pozycji");
        testPozycjaButton.setOnAction(e -> testPrzeniesSamochod());

        bottomSection.getChildren().addAll(dodajButton, usunButton, testPozycjaButton);
        return bottomSection;
    }

    private VBox createLeftSection() {
        VBox leftSection = new VBox(8);
        leftSection.setPadding(new Insets(8));
        leftSection.setMinWidth(280);

        TitledPane samochodPane = createSamochodPane();
        TitledPane skrzyniaPane = createSkrzyniaBiegowPane();
        TitledPane silnikPane = createSilnikPane();
        TitledPane sprzegloPane = createSprzegloPane();

        leftSection.getChildren().addAll(samochodPane, skrzyniaPane, silnikPane, sprzegloPane);
        return leftSection;
    }

    private TitledPane createSamochodPane() {
        VBox content = new VBox(4);

        content.getChildren().addAll(
                createLabeledTextField("Model:", "Samochód 1"),
                createLabeledTextField("Nr rejestracyjny:", "ABC123"),
                createLabeledTextField("Waga:", (silnik.getWaga() + skrzyniaBiegow.getWaga() + sprzeglo.getWaga()) + " kg"),
                createLabeledTextField("Prędkość:", "0 km/h")
        );

        HBox buttons = new HBox(4);
        Button wlaczButton = new Button("Włącz");
        Button wylaczButton = new Button("Wyłącz");

        wlaczButton.setOnAction(e -> {
            samochod.wlaczSamochod();
            System.out.println("Samochód włączony! Obroty: " + silnik.obroty);
        });

        wylaczButton.setOnAction(e -> {
            samochod.wylaczSilnik();
            System.out.println("Samochód wyłączony! Obroty: " + silnik.obroty);
        });

        buttons.getChildren().addAll(wlaczButton, wylaczButton);
        content.getChildren().add(buttons);

        TitledPane pane = new TitledPane("Samochód", content);
        pane.setExpanded(true);
        return pane;
    }

    private TitledPane createSkrzyniaBiegowPane() {
        VBox content = new VBox(4);
        content.getChildren().addAll(
                createLabeledTextField("Nazwa:", skrzyniaBiegow.getNazwa()),
                createLabeledTextField("Cena:", skrzyniaBiegow.getCena() + " zł"),
                createLabeledTextField("Waga:", skrzyniaBiegow.getWaga() + " kg"),
                createLabeledTextField("Bieg:", String.valueOf(skrzyniaBiegow.aktualnyBieg))
        );

        HBox buttons = new HBox(4);
        Button zwiekszButton = new Button("Zwiększ bieg");
        Button zmniejszButton = new Button("Zmniejsz bieg");

        zwiekszButton.setOnAction(e -> {
            skrzyniaBiegow.zwiekszBieg();
            System.out.println("Bieg zwiększony: " + skrzyniaBiegow.aktualnyBieg);
        });

        zmniejszButton.setOnAction(e -> {
            skrzyniaBiegow.zmniejszBieg();
            System.out.println("Bieg zmniejszony: " + skrzyniaBiegow.aktualnyBieg);
        });

        buttons.getChildren().addAll(zwiekszButton, zmniejszButton);
        content.getChildren().add(buttons);

        return new TitledPane("Skrzynia Biegów", content);
    }

    private TitledPane createSilnikPane() {
        VBox content = new VBox(4);
        content.getChildren().addAll(
                createLabeledTextField("Nazwa:", silnik.getNazwa()),
                createLabeledTextField("Cena:", silnik.getCena() + " zł"),
                createLabeledTextField("Waga:", silnik.getWaga() + " kg"),
                createLabeledTextField("Obroty:", silnik.obroty + " RPM")
        );

        HBox buttons = new HBox(4);
        Button dodajGazuButton = new Button("Dodaj gazu");
        Button ujmijGazuButton = new Button("Ujmij gazu");

        dodajGazuButton.setOnAction(e -> {
            System.out.println("Dodaj gazu - funkcja do implementacji");
        });

        ujmijGazuButton.setOnAction(e -> {
            System.out.println("Ujmij gazu - funkcja do implementacji");
        });

        buttons.getChildren().addAll(dodajGazuButton, ujmijGazuButton);
        content.getChildren().add(buttons);

        return new TitledPane("Silnik", content);
    }

    private TitledPane createSprzegloPane() {
        VBox content = new VBox(4);
        content.getChildren().addAll(
                createLabeledTextField("Nazwa:", sprzeglo.getNazwa()),
                createLabeledTextField("Cena:", sprzeglo.getCena() + " zł"),
                createLabeledTextField("Waga:", sprzeglo.getWaga() + " kg"),
                createLabeledTextField("Stan:", sprzeglo.stanSprzegla ? "Wciśnięte" : "Zwolnione")
        );

        HBox buttons = new HBox(4);
        Button wcisnijButton = new Button("Wciśnij");
        Button zwolnijButton = new Button("Zwolnij");

        wcisnijButton.setOnAction(e -> {
            sprzeglo.wcisnij();
            System.out.println("Sprzęgło wciśnięte");
        });

        zwolnijButton.setOnAction(e -> {
            sprzeglo.zwolnij();
            System.out.println("Sprzęgło zwolnione");
        });

        buttons.getChildren().addAll(wcisnijButton, zwolnijButton);
        content.getChildren().add(buttons);

        return new TitledPane("Sprzęgło", content);
    }

    private HBox createLabeledTextField(String labelText, String value) {
        HBox row = new HBox(5);
        Label label = new Label(labelText);
        label.setMinWidth(100);
        TextField textField = new TextField(value);
        textField.setEditable(false);
        textField.setPrefWidth(120);
        row.getChildren().addAll(label, textField);
        return row;
    }
}