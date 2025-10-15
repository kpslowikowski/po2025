import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class Lotto2 {


    private static ArrayList<Integer> losujUnikalne(int ile, int max) {
        ArrayList<Integer> wynik = new ArrayList<>();
        Random rand = new Random();
        while (wynik.size() < ile) {
            int liczba = rand.nextInt(max) + 1; // 1..max
            if (!wynik.contains(liczba)) {
                wynik.add(liczba);
            }
        }
        Collections.sort(wynik);
        return wynik;
    }

    public static void main(String[] args) {
        final int ILE = 6;
        final int MAX = 49;


        if (args.length != ILE) {
            System.err.println("Użycie: java Lotto <6 liczb od 1 do 49>");
            System.err.println("Przykład: java Lotto 1 2 3 5 8 9");
            return;
        }


        ArrayList<Integer> twoje = new ArrayList<>();
        Set<Integer> sprawdzUniqueness = new HashSet<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            int val;
            try {
                val = Integer.parseInt(a);
            } catch (NumberFormatException ex) {
                System.err.println("Błąd: argument '" + a + "' nie jest liczbą całkowitą.");
                return;
            }
            if (val < 1 || val > MAX) {
                System.err.println("Błąd: każda liczba musi być w zakresie 1.." + MAX + ". Błędna: " + val);
                return;
            }
            if (!sprawdzUniqueness.add(val)) {
                System.err.println("Błąd: liczby muszą być unikalne. Podano powtórzenie: " + val);
                return;
            }
            twoje.add(val);
        }


        ArrayList<Integer> twojePosortowane = new ArrayList<>(twoje);
        Collections.sort(twojePosortowane);


        ArrayList<Integer> wylosowane = losujUnikalne(ILE, MAX);


        int trafienia = 0;
        ArrayList<Integer> trafioneLista = new ArrayList<>();
        for (Integer t : twojePosortowane) {
            if (wylosowane.contains(t)) {
                trafienia++;
                trafioneLista.add(t);
            }
        }


        System.out.println("Twoje typy: " + twojePosortowane);
        System.out.println("Wylosowane liczby: " + wylosowane);
        System.out.println("Trafione liczby: " + trafioneLista);
        System.out.println("Liczba trafień: " + trafienia);
    }
}

