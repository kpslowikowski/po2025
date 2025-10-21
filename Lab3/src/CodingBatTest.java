import org.junit.Test;
import static org.junit.Assert.*;

public class CodingBatTest {

    public void testNotString() {
        // Przypadek 1: zwykłe słowo, więc dodajemy "not "
        assertEquals("not candy", CodingBat.notString("candy"));

        // Przypadek 2: jednoznakowy ciąg – też dodajemy "not "
        assertEquals("not x", CodingBat.notString("x"));

        // Przypadek 3: już zaczyna się od "not", więc zwracamy bez zmian
        assertEquals("not bad", CodingBat.notString("not bad"));
    }

    @org.junit.Test
    public void notString() {
    }
}