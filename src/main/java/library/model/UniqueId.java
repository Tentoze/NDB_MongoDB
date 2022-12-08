package library.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;
@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor

public class UniqueId {
    private UUID id;

    UniqueId(UUID id) {
        this.id = id;
    }


    public UUID getUUID() {
        return id;
    }
}
