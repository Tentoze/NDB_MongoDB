package library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

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
    @BsonProperty("isarchive")
    private boolean isArchive;

    @BsonCreator
    public Book(@BsonProperty("_id") UUID enitityId,
                @BsonProperty("title") String title,
                @BsonProperty("author") String author,
                @BsonProperty("serialnumber") String serialNumber,
                @BsonProperty("genre") String genre) {
        super(enitityId);
        this.title = title;
        this.author = author;
        this.serialNumber = serialNumber;
        this.genre = genre;
        this.isArchive = false;
    }

    public Book(String serialNumber, String title, String author, String genre) {
        super(UUID.randomUUID());
        this.serialNumber = serialNumber;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
