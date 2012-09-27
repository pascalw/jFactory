package nl.pwiddershoven.jfactory.models;

public class Book {

    private final String title;
    private final String author;
    private int price = 10;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPrice() {
        return price;
    }
}
