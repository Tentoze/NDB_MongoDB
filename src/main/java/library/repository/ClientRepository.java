package library.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import library.model.Client;
import org.bson.conversions.Bson;

public class ClientRepository extends AbstractMongoRepository<Client> {


    public ClientRepository() {
        super("clients", Client.class);
    }

    public Client findByPersonalID(String personalID) {
        MongoCollection<Client> collection = mongoDatabase.getCollection(collectionName, Client.class);
        Bson filter = Filters.eq("personalid", personalID);
        return collection.find().filter(filter).first();
    }
}
