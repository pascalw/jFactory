package nl.kabisa.jFactory;

public class Article {

    private String guid;
    private String title;

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

    @Override
    public String toString() {
        return "Article{" +
                "guid='" + guid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
