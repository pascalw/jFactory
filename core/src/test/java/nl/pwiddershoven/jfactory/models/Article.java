package nl.pwiddershoven.jfactory.models;

public class Article {

    public enum State {
        PUBLISHED, UNPUBLISHED
    }

    private int id;
    private String guid;
    private String title;
    private boolean read;
    private State state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Article{" +
                "guid='" + guid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
