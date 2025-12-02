package symulator;

public class Samochod {
    private String model;
    private String nrRej;
    private double waga;
    private double predkosc;

    Silnik silnik;
    SkrzyniaBiegow skrzyniaBiegow;
    Pozycja pozycja;
    Sprzeglo sprzeglo;

    public Samochod(String model, String nrRej, double waga, double predkosc,
                    Silnik silnik, SkrzyniaBiegow skrzyniaBiegow, Pozycja pozycja, Sprzeglo sprzeglo) {
        this.model = model;
        this.nrRej = nrRej;
        this.waga = waga;
        this.predkosc = predkosc;
        this.silnik = silnik;
        this.skrzyniaBiegow = skrzyniaBiegow;
        this.pozycja = pozycja;
        this.sprzeglo = sprzeglo;
    }

    // Konstruktor bez parametrów dla testów
    public Samochod(Silnik silnik, SkrzyniaBiegow skrzyniaBiegow, Pozycja pozycja, Sprzeglo sprzeglo) {
        this("Samochód Sportowy", "SPT-1234", 1500.0, 0.0,
                silnik, skrzyniaBiegow, pozycja, sprzeglo);
    }

    public void wlaczSamochod() {
        silnik.wlaczSilnik();
    }

    public void wylaczSilnik() {
        silnik.wylaczSilnik();
        skrzyniaBiegow.biegZero();
    }

    // GETTERY
    public String getModel() {
        return model;
    }

    public String getNrRej() {
        return nrRej;
    }

    public double getWaga() {
        return waga;
    }

    public double getPredkosc() {
        return predkosc;
    }

    public Silnik getSilnik() {
        return silnik;
    }

    public SkrzyniaBiegow getSkrzyniaBiegow() {
        return skrzyniaBiegow;
    }

    public Pozycja getPozycja() {
        return pozycja;
    }

    public Sprzeglo getSprzeglo() {
        return sprzeglo;
    }

    // SETTERY
    public void setModel(String model) {
        this.model = model;
    }

    public void setNrRej(String nrRej) {
        this.nrRej = nrRej;
    }

    public void setWaga(double waga) {
        this.waga = waga;
    }

    public void setPredkosc(double predkosc) {
        this.predkosc = predkosc;
    }
}