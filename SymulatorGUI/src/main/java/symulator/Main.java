package symulator;

public class Main {
    public static void main(String[] args) {
        Silnik silnik = new Silnik("silnik", 50, 100, 3000, 30);
        SkrzyniaBiegow skrzyneczka = new SkrzyniaBiegow("skrzynia", 100, 100, 1, 10, 10);
        Pozycja pozycja = new Pozycja(0, 0);
        Sprzeglo sprzeglo = new Sprzeglo("sprzeglo", 20, 50, false);
        Samochod samochod = new Samochod(silnik, skrzyneczka, pozycja, sprzeglo);

        samochod.wlaczSamochod();
        System.out.println("Silnik włączony! Obroty: " + silnik.getObroty());

        samochod.getPozycja().UaktualnijPozycje(3, 3);
        samochod.getPozycja().przenies(4, 2, 1);

        String aa = samochod.getPozycja().getPozycja();
        System.out.println(aa);
    }
}