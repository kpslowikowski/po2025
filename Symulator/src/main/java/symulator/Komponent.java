package symulator;

public abstract class Komponent {
    protected String nazwa;
    protected int waga;
    protected int cena;

    protected Komponent(String nazwa, int waga, int cena) {
        this.nazwa = nazwa;
        this.waga = waga;
        this.cena = cena;
    }


    public String getNazwa() {
        return nazwa;
    }

    public int getWaga() {
        return waga;
    }

    public int getCena() {
        return cena;
    }



}
