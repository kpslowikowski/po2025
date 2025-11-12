package symulator;

public class Main {
    public static void main(String[] args) {


        Silnik silnik = new Silnik("silnik", 50, 100, 30, 30);
        SkrzyniaBiegow skrzyneczka = new SkrzyniaBiegow("skrzynia", 100, 100, 1, 10, 10);
        Pozycja pozycja = new Pozycja(0, 0);
        Samochod samochod = new Samochod(silnik, skrzyneczka, pozycja );
        samochod.pozycja.UaktualnijPozycje(3, 3);
        samochod.pozycja.przenies(4,2,1);
        String aa = samochod.pozycja.getPozycja();
        System.out.println(aa);
    }
}
