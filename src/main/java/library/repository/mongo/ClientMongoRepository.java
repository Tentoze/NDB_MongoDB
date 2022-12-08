package library.repository.mongo;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import library.model.Client;
import org.bson.conversions.Bson;

public class ClientMongoRepository extends AbstractMongoRepository<Client> {


    public ClientMongoRepository() {
        super("clients", Client.class);
    }

    public Client findByPersonalID(String personalID) {
        MongoCollection<Client> collection = mongoDatabase.getCollection(collectionName, Client.class);
        Bson filter = Filters.eq("personalid", personalID);
        return collection.find().filter(filter).first();
    }

    public void update(Client client) {
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            MongoCollection<Client> clientsCollection = mongoDatabase.getCollection(collectionName, Client.class);
            Bson filter = Filters.eq("_id", client.getEntityId());

            Bson setUpdate = Updates.combine(
                    Updates.set("firstname", client.getFirstName()),
                    Updates.set("lastname", client.getLastName()),
                    Updates.set("personalid", client.getPersonalID()),
                    Updates.set("age", client.getAge()),
                    Updates.set("archived", client.isArchived()),
                    Updates.set("debt", client.getDebt())

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
        MongoCollection<Client> collection = mongoDatabase.getCollection(collectionName, Client.class);
        collection.drop();
    }
}
