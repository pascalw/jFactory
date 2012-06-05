package nl.kabisa.jFactory;

public abstract class Trait extends BasicFactory {

    private String name;

    public Trait(String name) {
        this.name = name;
        define();
    }

    public String getName() {
        return name;
    }

    protected abstract void define();
}
