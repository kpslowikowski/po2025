package Animals;

public class Dog extends Abstract_animal{
    public Dog(String name, int legs) {
        super(name, 4);
    }

    @Override
    public String getDescryption() {
        return "Zwierzę" + name + "ma " + legs + " nogi";
    }

}
