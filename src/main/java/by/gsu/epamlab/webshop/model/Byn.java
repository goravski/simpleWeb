package by.gsu.epamlab.webshop.model;

public class Byn implements Comparable<Byn> {
    private int value;

    public Byn(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Byn(int rubs, int coins) {
        this(rubs * 100 + coins);
    }

    public Byn(Byn byn) {
        this(byn.value);
    }

    public Byn add(Byn byn) {
        return new Byn(value + byn.value);
    }

    public Byn sub(Byn byn) {
        return new Byn(value - byn.value);
    }

    public Byn mul(int k) {
        return new Byn(value * k);
    }

    public Byn mul(double k, RoundMethod roundMethod, int d) {
        return new Byn(roundMethod.round(value * k, d));
    }

    public Byn round(RoundMethod roundMethod, int d) {
        return new Byn(roundMethod.round(value, d));
    }

    @Override
    public String toString() {
        return String.format("%d.%d%d", value / 100, value % 100 / 10, value % 10);
    }

    @Override
    public int compareTo(Byn o) {
        return value - o.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Byn byn = (Byn) o;
        return value == byn.value;
    }
}


