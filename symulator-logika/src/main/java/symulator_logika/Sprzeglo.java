package symulator_logika;


public class Sprzeglo extends Komponent {
    boolean stanSprzegla;

    public Sprzeglo(String nazwa, int waga, int cena, boolean stanSprzegla) {
        super(nazwa, waga, cena);
        this.stanSprzegla = stanSprzegla;
    }

    public void wcisnij() {
        stanSprzegla = true;
    }

    public void zwolnij() {
        stanSprzegla = false;
    }
}
