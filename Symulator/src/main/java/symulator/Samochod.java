package symulator;

public class Samochod {
    Silnik silnik;
    SkrzyniaBiegow skrzyniaBiegow;
    Pozycja pozycja;


    public Samochod(Silnik silnik, SkrzyniaBiegow skrzyniaBiegow, Pozycja pozycja) {
        this.silnik = silnik;
        this.skrzyniaBiegow = skrzyniaBiegow;
        this.pozycja = pozycja;
    }

    public void wlaczSamochod() {
        silnik.wlaczSilnik();
    }
    public void wylaczSilnik() {
        silnik.wylaczSilnik();
        skrzyniaBiegow.biegZero();
    }
}
