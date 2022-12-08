package library.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
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
        super(new UniqueId(UUID.randomUUID()));
        this.serialNumber = serialNumber;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isRented = 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("serialNumber='").append(serialNumber).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", genre='").append(genre).append('\'');
        sb.append(", isArchived=").append(isArchived);
        sb.append(", isRented=").append(isRented);
        sb.append('}');
        return sb.toString();
    }
}
