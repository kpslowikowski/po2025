package symulator;

public class Main {
    Silnik silnik = new Silnik("silnik", 50, 100, 30, 30);
    SkrzyniaBiegow skrzyneczka = new SkrzyniaBiegow("skrzynia", 100, 100, 1, 10, 10);
    Samochod samochod = new Samochod(silnik, skrzyneczka);
}
