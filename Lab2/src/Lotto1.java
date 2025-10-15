import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
 public class Lotto1 {
     public static void main(String[] args) {
         ArrayList<Integer> liczby = new ArrayList<>();
         Random rand = new Random();
          while (liczby.size() < 6) {
              int liczba = rand.nextInt(49) + 1;
              if (!liczby.contains(liczba)) {
                  liczby.add(liczba);
              }


          }
          Collections.sort(liczby);
          System.out.println(liczby);
     }
 }