package library.model;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;


@Getter
@Setter
public abstract class Client extends AbstractEntity {

    @BsonProperty("personalid")
    private String personalID;
    @BsonProperty("firstname")
    private String firstName;
    @BsonProperty("lastname")
    private String lastName;
    @BsonProperty("age")
    private Integer age;
    @BsonProperty("isarchive")
    private boolean isArchive;
    @BsonProperty("debt")
    private Float debt;

    @BsonCreator
    public Client(@BsonProperty("_id") UUID enitityId,
                  @BsonProperty("firstname") String firstName,
                  @BsonProperty("lastname") String lastName,
                  @BsonProperty("personalid") String personalID,
                  @BsonProperty("age") Integer age) {
        super(enitityId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.age = age;
        this.isArchive = false;
        this.debt = (float) 0.0;
    }

    public Client(String personalID, String firstName, String lastName, Integer age) {
        super(UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.age = age;
        this.isArchive = false;
        this.debt = (float) 0.0;
    }

    public abstract Float getPenalty();

    public abstract Integer getMaxDays();

    public abstract Integer getMaxBooks();

}
