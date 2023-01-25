package library.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@BsonDiscriminator(key = "_clazz", value = "adult")
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
@JsonTypeName("adult")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Adult.class, name = "adult")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Adult extends Client {

@BsonCreator
    public Adult(@BsonProperty("firstname") String firstName,
            @BsonProperty("lastname") String lastName,
            @BsonProperty("personalid") String personalID,
            @BsonProperty("age") Integer age) {
        super(firstName, lastName, personalID, age);
    }
    @JsonCreator
    public Adult(@JsonProperty("_id") UniqueId entityId,
                 @JsonProperty("firstname") String firstName,
                 @JsonProperty("lastname") String lastName,
                 @JsonProperty("personalid") String personalID,
                 @JsonProperty("isarchived") boolean isArchived,
                 @JsonProperty("debt") Float debt,
                 @JsonProperty("age") Integer age,
                 @JsonProperty("penalty") Float penalty) {
        super(entityId, firstName, lastName, personalID, isArchived, debt, age);
    }

    @Override
    public Float getPenalty() {
        return (float) (5 * 2);
    }

    @Override
    public Integer getMaxDays() {
        return 90;
    }

    @Override
    public Integer getMaxBooks() {
        return 5;
    }
}
