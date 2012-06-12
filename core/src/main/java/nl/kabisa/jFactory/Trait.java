package nl.kabisa.jFactory;

public abstract class Trait {

    private String name;

    public Trait(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract void define();
}
