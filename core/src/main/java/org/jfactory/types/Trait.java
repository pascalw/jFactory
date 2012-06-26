package org.jfactory.types;

public abstract class Trait {

    private String name;

    public Trait(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    abstract public void apply();
}
