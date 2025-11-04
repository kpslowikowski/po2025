package symulator;

public class Pozycja {
    double x;
    double y;
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
}
