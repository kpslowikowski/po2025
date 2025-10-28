package Animals;

public abstract class Abstract_animal {
    protected String name;
    protected int legs;

    public Abstract_animal(String name, int legs) {
        this.name = name;
        this.legs = legs;
    }

    public int getLegs() {
        return legs;
    }

    public abstract String getDescryption();


}