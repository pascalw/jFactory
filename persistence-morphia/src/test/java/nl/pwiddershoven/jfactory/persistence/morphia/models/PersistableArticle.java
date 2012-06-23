package nl.pwiddershoven.jfactory.persistence.morphia.models;

import com.google.code.morphia.annotations.Id;
import org.bson.types.ObjectId;

public class PersistableArticle {

    @Id
    private ObjectId id;

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
        return "PersistableArticle{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
