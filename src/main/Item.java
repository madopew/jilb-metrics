package main;

public class Item {
    private String name;
    private int id;
    private int counter;

    public Item(String name, int counter, int id) {
        this.name = name;
        this.counter = counter;
        this.id = id;
    }

    public String getItem() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getCounter() {
        return counter;
    }

    public void setItem(String item) {
        this.name = item;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}