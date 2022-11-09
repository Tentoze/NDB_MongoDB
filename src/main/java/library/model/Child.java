package library.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@BsonDiscriminator(key = "_clazz",value = "child")
public class Child extends Client {
    @BsonCreator
    public Child(@BsonProperty("firstname") String firstName,
                 @BsonProperty("lastname") String lastName,
                 @BsonProperty("personalid") String personalID,
                 @BsonProperty("age") Integer age) {
        super(firstName, lastName, personalID, age);
    }

    @Override
    public Float getPenalty() {
        return (float)(5*1.2);
    }

    @Override
    public Integer getMaxDays() {
        return 60;
    }

    @Override
    public Integer getMaxBooks() {
        return 3;
    }

}
