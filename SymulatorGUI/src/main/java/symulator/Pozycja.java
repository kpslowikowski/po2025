package symulator;

public class Pozycja {
    double x = 0;
    double y = 0;

    public Pozycja(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String getPozycja() {
        return "Pozycja: " + x + ", " + y;
    }

    public void UaktualnijPozycje(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // STARA SYGNATURA: przenies(double x1, double y1, int v)
    // NOWA SYGNATURA: przenies(double xCel, double yCel, double predkosc, double deltat)

    // Przywracamy starą sygnaturę, ale poprawiamy logikę:
    public void przenies(double xCel, double yCel, int predkosc) {
        double dx = xCel - x;
        double dy = yCel - y;
        double odleglosc = Math.sqrt(dx * dx + dy * dy);

        if (odleglosc > 1.0) { // Jeśli nie jesteśmy blisko celu
            // Oblicz wektor kierunku
            double kierunekX = dx / odleglosc;
            double kierunekY = dy / odleglosc;

            // Używamy deltat = 0.1 (jak w instrukcji)
            double deltat = 0.1;
            // Prędkość w pikselach na sekundę (przeliczamy z km/h)
            double przesuniecie = (predkosc * 1000.0 / 3600.0) * deltat * 10;

            // Jeśli przesunięcie jest większe niż odległość do celu, idź bezpośrednio do celu
            if (przesuniecie > odleglosc) {
                this.x = xCel;
                this.y = yCel;
            } else {
                // W przeciwnym razie przesuń się w kierunku celu
                this.x += kierunekX * przesuniecie;
                this.y += kierunekY * przesuniecie;
            }
        } //else {
            // Jesteśmy już blisko celu - zatrzymaj się
            //this.x = xCel;
            //this.y = yCel;
        //}
    }

    // GETTERY
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // SETTERY
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}