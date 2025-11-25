package symulator_logika;

public class Silnik extends Komponent{
    int maxObroty;
    int obroty;

    public Silnik(String nazwa, int waga, int cena, int maxObroty, int obroty) {
        super(nazwa, waga, cena);
        this.maxObroty = maxObroty;
        this.obroty = obroty;

    }
    public void wlaczSilnik() {
        obroty = 880;

    }
    public void wylaczSilnik() {
        obroty = 0;

    }

}