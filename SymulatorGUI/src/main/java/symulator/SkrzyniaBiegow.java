package symulator;

public class SkrzyniaBiegow extends Komponent{
    int aktualnyBieg;
    int iloscBiegow;
    int aktualnePrzelozenie;

    public SkrzyniaBiegow(String nazwa, int waga, int cena, int aktualnyBieg, int aktualnePrzelozenie, int iloscBiegow) {
        super(nazwa, waga, cena);
        this.aktualnyBieg =  aktualnyBieg;
        this.iloscBiegow = iloscBiegow;
        this.aktualnePrzelozenie = aktualnePrzelozenie;
    }

    public void biegZero() {
        aktualnyBieg = 0;
    }

    public void zwiekszBieg() {
        if (aktualnyBieg < iloscBiegow) {
            aktualnyBieg += 1;
        }
    }

    public void zmniejszBieg() {
        if (aktualnyBieg > 0) {
            aktualnyBieg -= 1;
        }
    }

    // GETTERY
    public int getAktualnyBieg() {
        return aktualnyBieg;
    }

    public int getIloscBiegow() {
        return iloscBiegow;
    }

    public int getAktualnePrzelozenie() {
        return aktualnePrzelozenie;
    }
}