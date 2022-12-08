package library.repository.mongo;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import library.model.Book;
import library.model.Client;
import library.model.Rent;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class RentMongoRepository extends AbstractMongoRepository<Rent> {


    public RentMongoRepository() {
        super("rents", Rent.class);
    }
    public List<Rent> findByClient(Client client) {
        MongoCollection<Rent> collection = mongoDatabase.getCollection(collectionName, Rent.class);
        Bson filter = Filters.eq("client._id", client.getEntityId());
        return collection.find().filter(filter).into(new ArrayList<>());
    }
    public Rent findByBook(Book book) {
        MongoCollection<Rent> collection = mongoDatabase.getCollection(collectionName, Rent.class);
        Bson filter = Filters.eq("book._id", book.getEntityId());
        return collection.find().filter(filter).first();
    }
    public void update(Rent rent) {
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            MongoCollection<Rent> clientsCollection = mongoDatabase.getCollection(collectionName, Rent.class);
            Bson filter = Filters.eq("_id", rent.getEntityId());
            Bson setUpdate = Updates.combine(
                    Updates.set("client", rent.getClient()),
                    Updates.set("book", rent.getBook())
            );
            clientsCollection.updateOne(clientSession, filter, setUpdate);
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
        } finally {
            clientSession.close();
        }

    }
    public void clearDatabase() {
        MongoCollection<Rent> collection = mongoDatabase.getCollection(collectionName, Rent.class);
        collection.drop();
    }



}
