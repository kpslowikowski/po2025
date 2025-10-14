import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Lotto {
    public static List<Integer> drawSix() {
        List<Integer> pool = new ArrayList<>();
        for (int i = 1; i <= 49; i++) pool.add(i);
        Collections.shuffle(pool);
        List<Integer> result = new ArrayList<>(pool.subList(0, 6));
        Collections.sort(result);
        return result;
        }
    }
    public static List<Integer> parseSix(String[] args, int startIndex) {
        if (args.length -  startIndex < 6) return null;
        Set<Integer> set = new HashSet<>();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            try {
                int v = Integer.parseInt(args[])
            }
        }
}
