package auto_ui.symulatorgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField pozycjaXTextField;

    @FXML
    private TextField pozycjaYTextField;

    @FXML
    private ImageView carImageView;

    @FXML
    public void initialize() {
        // Załaduj obrazek samochodu
        try {
            Image carImage = new Image(getClass().getResourceAsStream("/auto_ui/symulatorgui/samochod.png"));
            carImageView.setImage(carImage);
            System.out.println("Obrazek samochodu załadowany!");
        } catch (Exception e) {
            System.out.println("Nie można załadować obrazka samochodu: " + e.getMessage());
            welcomeText.setText("Brak obrazka samochodu!");
        }

        // Ustaw początkową pozycję
        pozycjaXTextField.setText("100");
        pozycjaYTextField.setText("100");
        updateCarPosition(100, 100);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");

        // Przesuń samochód po kliknięciu (test)
        updateCarPosition(200, 150);
        pozycjaXTextField.setText("200");
        pozycjaYTextField.setText("150");
    }

    private void updateCarPosition(double x, double y) {
        carImageView.setTranslateX(x);
        carImageView.setTranslateY(y);
    }
}