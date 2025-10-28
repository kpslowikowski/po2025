package Animals;

public class Snake extends Abstract_animal{
    public Snake(String name, int legs) {
        super(name, 0);
    }

    @Override
    public String getDescryption() {
        return "Zwierzę " + name + " ma " + legs + " nogi";
    }
}
