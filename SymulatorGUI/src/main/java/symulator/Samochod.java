package symulator;

import java.util.ArrayList;
import java.util.List;

public class Samochod extends Thread {
    private String model;
    private String nrRej;
    private double waga;
    private double predkosc;

    Silnik silnik;
    SkrzyniaBiegow skrzyniaBiegow;
    Pozycja pozycja;
    Sprzeglo sprzeglo;

    private Pozycja cel = null;
    private List<Listener> listeners = new ArrayList<>();

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

        // Start wątku na końcu konstruktora
        this.start();
    }

    // Konstruktor bez parametrów dla testów
    public Samochod(Silnik silnik, SkrzyniaBiegow skrzyniaBiegow, Pozycja pozycja, Sprzeglo sprzeglo) {
        this("Samochód Sportowy", "SPT-1234", 1500.0, 100.0,
                silnik, skrzyniaBiegow, pozycja, sprzeglo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (cel != null && silnik.getObroty() > 0) { // Tylko jeśli silnik pracuje
                    // Oblicz aktualną prędkość w km/h na podstawie obrotów i biegu
                    double predkoscWspolczynnik = (double)silnik.getObroty() / (double)silnik.getMaxObroty();
                    double biegWspolczynnik = skrzyniaBiegow.getAktualnyBieg() == 0 ? 0 :
                            (double)skrzyniaBiegow.getAktualnyBieg() / (double)skrzyniaBiegow.getIloscBiegow();

                    // Prędkość efektywna = maksymalna prędkość * współczynnik obrotów * współczynnik biegu
                    int aktualnaPredkosc = (int)(predkosc * predkoscWspolczynnik * biegWspolczynnik);

                    System.out.println("Prędkość: " + aktualnaPredkosc + " km/h, Bieg: " +
                            skrzyniaBiegow.getAktualnyBieg() + ", Obroty: " + silnik.getObroty());

                    // Przenieś samochód używając STAREJ sygnatury: przenies(double, double, int)
                    pozycja.przenies(cel.getX(), cel.getY(), aktualnaPredkosc);

                    // Powiadom obserwatorów o zmianie pozycji
                    notifyListeners();

                    // Sprawdź czy dotarliśmy do celu
                    double odleglosc = Math.sqrt(
                            Math.pow(cel.getX() - pozycja.getX(), 2) +
                                    Math.pow(cel.getY() - pozycja.getY(), 2)
                    );

                    if (odleglosc < 5.0) { // Dotarliśmy blisko celu
                        cel = null;
                        System.out.println("Samochód " + model + " dotarł do celu");
                    }
                }

                Thread.sleep(100); // 100 ms = 0.1s

            } catch (InterruptedException e) {
                System.out.println("Wątek samochodu przerwany: " + e.getMessage());
                break;
            } catch (Exception e) {
                System.out.println("Błąd w wątku samochodu: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void jedzDo(Pozycja nowaPozycja) {
        this.cel = nowaPozycja;
        System.out.println("Samochód " + model + " jedzie do: (" + nowaPozycja.getX() + ", " + nowaPozycja.getY() + ")");
    }

    // Metody zarządzania listenerami
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    public void wlaczSamochod() {
        silnik.wlaczSilnik();
        notifyListeners();
    }

    public void wylaczSilnik() {
        silnik.wylaczSilnik();
        skrzyniaBiegow.biegZero();
        notifyListeners();
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
        notifyListeners();
    }
}