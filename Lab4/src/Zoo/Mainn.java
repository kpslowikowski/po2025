package Zoo;

public class Mainn {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        zoo.fillAnimals();
        System.out.println(zoo.sum());
        zoo.makeNoise();
    }
}