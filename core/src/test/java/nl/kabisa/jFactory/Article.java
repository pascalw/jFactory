package nl.kabisa.jFactory;

public class Article {

    private String guid;
    private String title;
    private boolean read;

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

    @Override
    public String toString() {
        return "Article{" +
                "guid='" + guid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
