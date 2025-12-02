package symulator;

public class Silnik extends Komponent {
    int maxObroty;
    int obroty;

    public Silnik(String nazwa, int waga, int cena, int maxObroty, int obroty) {
        super(nazwa, waga, cena);
        this.maxObroty = maxObroty;
        this.obroty = Math.min(obroty, maxObroty); // Bezpiecznik
    }

    public void wlaczSilnik() {
        obroty = Math.min(880, maxObroty); // Bezpiecznik
    }

    public void wylaczSilnik() {
        obroty = 0;
    }

    public void dodajGazu() {
        if (obroty < maxObroty - 100) {
            obroty += 100;
        } else if (obroty < maxObroty) {
            obroty = maxObroty; // Nie przekraczaj maksimum
        }

        // Bezpiecznik - nie przekraczaj maksymalnych obrotów
        if (obroty > maxObroty) {
            obroty = maxObroty;
            System.out.println("OSTRZEŻENIE: Osiągnięto maksymalne obroty!");
        }
    }

    public void ujmijGazu() {
        if (obroty > 100) {
            obroty -= 100;
        } else if (obroty > 0) {
            obroty = 0;
        }

        // Bezpiecznik - nie spadaj poniżej zera
        if (obroty < 0) {
            obroty = 0;
        }
    }
    // GETTERY
    public int getMaxObroty() {
        return maxObroty;
    }

    public int getObroty() {
        return obroty;
    }

    // Pozostałe metody...
}