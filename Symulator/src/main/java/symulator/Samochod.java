package symulator;

public class Samochod {
    Silnik silnik;
    SkrzyniaBiegow skrzyniaBiegow;


    public Samochod(Silnik silnik, SkrzyniaBiegow skrzyniaBiegow) {
        this.silnik = silnik;
        this.skrzyniaBiegow = skrzyniaBiegow;
    }

    public void wlaczSamochod() {
        silnik.wlaczSilnik();
    }
    public void wylaczSilnik() {
        silnik.wylaczSilnik();
        skrzyniaBiegow.biegZero();
    }
}
