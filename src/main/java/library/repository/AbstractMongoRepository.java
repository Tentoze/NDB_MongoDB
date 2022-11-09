package library.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import library.model.Adult;
import library.model.Child;
import library.model.Client;
import library.model.UniqueIdCodecProvider;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractMongoRepository<T> implements AutoCloseable {

    protected ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
    protected MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());
    protected CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .register(Client.class, Child.class, Adult.class)
            .conventions(Conventions.DEFAULT_CONVENTIONS)
            .build());
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;
    protected final String collectionName;
    private final Class<T> entityClass;

    public AbstractMongoRepository(String collectionName, Class<T> entityClass) {
        this.collectionName = collectionName;
        this.entityClass = entityClass;
        initDbConnection();
    }

    protected void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new UniqueIdCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry)
                ).build();
    mongoClient = MongoClients.create(settings);
    mongoDatabase = mongoClient.getDatabase("library");
    }

    public void add(T object) {
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, entityClass);
        collection.insertOne(object);
    }

    public T findById(UUID id) {
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, entityClass);
        Bson filter = Filters.eq("_id", id);
        return collection.find().filter(filter).first();
    }

    public List<T> findAll() {
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, entityClass);
        return collection.find().into(new ArrayList<>());
    }

    public void update(T object) {
    }

    public void delete(UUID id) {
        MongoCollection<T> collection = mongoDatabase.getCollection(collectionName, entityClass);
        Bson filter = Filters.eq("_id", id);
        collection.deleteOne(filter);
        ClientSession clientSession = mongoClient.startSession();
    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
