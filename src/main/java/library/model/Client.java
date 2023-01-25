package library.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Random;
import java.util.UUID;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_clazz")
@JsonbTypeInfo({
        @JsonbSubtype(alias = "child", type = Child.class),
        @JsonbSubtype(alias = "adult", type = Adult.class)
})
@Getter
@Setter
@BsonDiscriminator(key = "_clazz")
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
@JsonSubTypes({
        @JsonSubTypes.Type(value = Adult.class, name = "adult"),
        @JsonSubTypes.Type(value = Child.class, name = "child")
})
public abstract class Client extends AbstractEntity {

    @BsonProperty("personalid")
    private String personalID;
    @BsonProperty("firstname")
    private String firstName;
    @BsonProperty("lastname")
    private String lastName;
    @BsonProperty("age")
    private Integer age;
    @BsonProperty("isarchived")
    private boolean isArchived;
    @BsonProperty("debt")
    private Float debt;

    @BsonCreator
    public Client(@BsonProperty("_id") UniqueId entityId,
                  @BsonProperty("firstname") String firstName,
                  @BsonProperty("lastname") String lastName,
                  @BsonProperty("personalid") String personalID,
                  @BsonProperty("isarchived") boolean isArchived,
                  @BsonProperty("debt") Float debt,
                  @BsonProperty("age") Integer age) {
        super(entityId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.age = age;
        this.isArchived = isArchived;
        this.debt = debt;
    }

    public Client(String firstName, String lastName,  String personalID, Integer age) {
        super(new UniqueId(UUID.randomUUID()));
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.age = age;
        this.isArchived = false;
        this.debt = (float) 0.0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Client{");
        sb.append("personalID='").append(personalID).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", age=").append(age);
        sb.append(", isArchived=").append(isArchived);
        sb.append(", debt=").append(debt);
        sb.append('}');
        return sb.toString();
    }

    public abstract Float getPenalty();

    public abstract Integer getMaxDays();

    public abstract Integer getMaxBooks();

}
