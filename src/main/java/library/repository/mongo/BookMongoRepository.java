package library.repository.mongo;


import com.mongodb.MongoClientSettings;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.ValidationOptions;
import library.model.Book;
import library.model.UniqueIdCodecProvider;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;

public class BookMongoRepository extends AbstractMongoRepository<Book> {


    public BookMongoRepository() {
        super("books", Book.class);
    }
    public Book findBySerialNumber(String serialNumber) {
        MongoCollection<Book> collection = mongoDatabase.getCollection(collectionName, Book.class);
        Bson filter = Filters.eq("serialnumber", serialNumber);
        return collection.find().filter(filter).first();
    }

    public void update(Book book) {

        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            MongoCollection<Book> booksCollection = mongoDatabase.getCollection(collectionName, Book.class);
            Bson filter = Filters.eq("_id", book.getEntityId());

            Bson setUpdate = Updates.combine(
                    Updates.set("title", book.getTitle()),
                    Updates.set("author", book.getAuthor()),
                    Updates.set("serialnumber", book.getSerialNumber()),
                    Updates.set("genre", book.getGenre()),
                    Updates.set("archived",book.isArchived()),
                    Updates.set("rented",book.getIsRented())
            );

            booksCollection.updateOne(clientSession, filter, setUpdate);
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
        } finally {
            clientSession.close();
        }

    }

    public void clearDatabase() {
        MongoCollection<Book> collection = mongoDatabase.getCollection(collectionName, Book.class);
        collection.drop();
    }

    public void incrementIsRented(Book book) {
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            MongoCollection<Book> booksCollection = mongoDatabase.getCollection(collectionName, Book.class);
            Bson filter = Filters.eq("_id", book.getEntityId());
            Bson update = Updates.inc("rented",1);
            booksCollection.updateOne(clientSession,filter,update);
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
        } finally {
            clientSession.close();
        }
    }

    @Override
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
            ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "required": ["_id","rented"]
                                "properties": {
                                    "rented": {
                                        "bsonType" : "int",
                                        "minimum" : 0,
                                        "maximum" : 1
                                    }
                                }
                            }
                        }
                        """));
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
            try {
                mongoDatabase.createCollection("books", createCollectionOptions);
            } catch (Exception e) {

            }
    }
}
