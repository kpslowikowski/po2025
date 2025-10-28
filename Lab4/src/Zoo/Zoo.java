package Zoo;
import Animals.Abstract_animal;
import Animals.Dog;
import Animals.Parrot;
import Animals.Snake;
import java.util.Random;
public class Zoo {
    private Abstract_animal[] animals = new Abstract_animal[100];
    private Random rand = new Random();
    public void fillAnimals() {
        for (int i = 0; i < animals.length; i++) {
            int AnimalType = rand.nextInt(3);
            int iloscPsow = 0;
            int iloscPapug = 0;
            int iloscWazow = 0;
            switch (AnimalType) {
                case 0:
                    iloscPsow ++;
                    animals[i] = new Dog("Pies" + iloscPsow, 4);
                    break;
                case 1:
                    iloscPapug ++;
                    animals[i] = new Parrot("Papuga" + iloscPapug, 2);
                    break;
                case 2:
                    iloscWazow ++;
                    animals[i] = new Snake("wąż" + iloscWazow, 0);
                    break;

            }


        }
    }
    public int sum() {
        int sum = 0;
        for (Abstract_animal animal : animals) {
            sum += animal.getLegs();

        }
        return sum;
    }

    public void makeNoise() {
        for (Abstract_animal animal : animals) {
            String s = animal.getDescryption();
            System.out.println(s);
        }
    }

    public Abstract_animal[] getAnimals() {
        return animals;

    }
}
