package symulator;

public class Pozycja {
    double x = 0;
    double y = 0;
    double czas = 0.1;

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

    public void przenies(double x1, double y1, int v) {
        double xnew = x + (v * czas * (x1 - x));
        double ynew = y + (v * czas * (y1 - y));
        this.x = xnew;
        this.y = ynew;
    }

    // GETTERY
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}