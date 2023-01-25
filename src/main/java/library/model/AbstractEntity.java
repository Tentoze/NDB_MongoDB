package library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.UUID;
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractEntity implements Serializable {

    @BsonProperty("_id")
    @BsonId
    protected UniqueId entityId;

    @BsonCreator
    public AbstractEntity(@BsonProperty UniqueId entityId) {
        this.entityId = entityId;
    }
}
