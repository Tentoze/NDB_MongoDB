package library.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@Setter
public class Book extends AbstractEntity {

    @BsonProperty("serialnumber")
    private String serialNumber;
    @BsonProperty("title")
    private String title;
    @BsonProperty("author")
    private String author;
    @BsonProperty("genre")
    private String genre;
    @BsonProperty("archived")
    private boolean isArchived;
    @BsonProperty("rented")
    private int isRented;

    @BsonCreator
    public Book(@BsonProperty("_id") UniqueId enitityId,
                @BsonProperty("title") String title,
                @BsonProperty("author") String author,
                @BsonProperty("serialnumber") String serialNumber,
                @BsonProperty("archived") boolean isArchived,
                @BsonProperty("rented") int isRented,
                @BsonProperty("genre") String genre) {
        super(enitityId);
        this.title = title;
        this.author = author;
        this.serialNumber = serialNumber;
        this.genre = genre;
        this.isRented = isRented;
        this.isArchived = isArchived;
    }

    public Book(String title, String author, String serialNumber, String genre) {
        super(new UniqueId());
        this.serialNumber = serialNumber;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isRented = 0;
    }
}
