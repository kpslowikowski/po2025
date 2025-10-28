package Animals;

public class Parrot extends Abstract_animal{
    public Parrot(String name, int legs) {
        super(name, 2);
    }

    @Override
    public String getDescryption() {
        return "Zwierzę " + name + " ma " + legs + " nogi";
    }
}
