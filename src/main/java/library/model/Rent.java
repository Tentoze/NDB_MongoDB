package library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
public class Rent extends AbstractEntity {

    @BsonProperty("begintime")
    private Date beginTime;
    @BsonProperty("endtime")
    private Date endTime;
    @BsonProperty(value = "client", useDiscriminator = true)
    private Client client;
    @BsonProperty("book")
    private Book book;
    @BsonCreator
    public Rent(@BsonProperty("_id") UniqueId entityId,
                @BsonProperty("client") Client client,
                @BsonProperty("book")Book book,
                @BsonProperty("begintime") Date begintime,
                @BsonProperty("endtime") Date endtime) {
        super(entityId);
        this.client = client;
        this.beginTime = begintime;
        this.book = book;
        this.endTime = endtime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rent{");
        sb.append("beginTime=").append(beginTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", client=").append(client);
        sb.append(", book=").append(book);
        sb.append('}');
        return sb.toString();
    }

    public Rent(Client client, Book book) {
        super(new UniqueId(UUID.randomUUID()));
        this.client = client;
        this.book = book;
        this.beginTime = new Date();
        this.endTime = calculateEndTime(beginTime);
    }

    private Date calculateEndTime(Date beginTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        calendar.add(Calendar.DATE,client.getMaxDays());
        return calendar.getTime();
    }
}
