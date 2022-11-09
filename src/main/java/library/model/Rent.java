package library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@BsonDiscriminator
public class Rent extends AbstractEntity {

    @BsonProperty("begintime")
    private Date beginTime;
    @BsonProperty("endtime")
    private Date endTime;
    @BsonProperty(value = "client", useDiscriminator = true)
    private Client client;
    @BsonProperty("book")
    private Book book;

    public Rent(@BsonProperty("_id") UUID entityId,
                @BsonProperty("client") Client client,
                @BsonProperty("book")Book book) {
        super(entityId);
        this.client = client;
        this.beginTime = new Date();
        this.book = book;
        this.endTime = calculateEndTime(beginTime);
    }


    private Date calculateEndTime(Date beginTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        calendar.add(Calendar.DATE,client.getMaxDays());
        return calendar.getTime();
    }
}
