package library.model;


import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;


@Getter
@Setter
@BsonDiscriminator(key = "_clazz")
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
    public Client(@BsonProperty("_id") UniqueId enitityId,
                  @BsonProperty("firstname") String firstName,
                  @BsonProperty("lastname") String lastName,
                  @BsonProperty("personalid") String personalID,
                  @BsonProperty("isarchived") boolean isArchived,
                  @BsonProperty("debt") Float debt,
                  @BsonProperty("age") Integer age) {
        super(enitityId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.age = age;
        this.isArchived = isArchived;
        this.debt = debt;
    }

    public Client(String firstName, String lastName,  String personalID, Integer age) {
        super(new UniqueId());
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
