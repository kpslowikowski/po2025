public class CodingBat {

    // Metoda notString - dodaje "not " na początek, jeśli ciąg nie zaczyna się od "not"
    public static String notString(String str) {
        // Sprawdzamy, czy str zaczyna się od "not"
        if (str.length() >= 3 && str.substring(0, 3).equals("not")) {
            return str;
        }
        // Dodajemy "not " na początek
        return "not " + str;
    }

    // Metoda main - do testowania funkcji notString
    public static void main(String[] args) {
        // Testy dla metody notString
        System.out.println(notString("candy"));  // Powinno zwrócić "not candy"
        System.out.println(notString("x"));      // Powinno zwrócić "not x"
        System.out.println(notString("not bad")); // Powinno zwrócić "not bad"
    }
}