package library.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor

public class UniqueId {

    private UUID id;


    UniqueId(UUID id) {
        this.id = id;
    }

    @JsonIgnore
    public UUID getUUID() {
        return id;
    }
}
