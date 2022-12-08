package library.repository.redis;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import library.model.Client;
import library.model.Rent;
import org.bson.conversions.Bson;

import java.util.Optional;

public class ClientRedisRepository extends AbstractRedisRepository<Client>{
    public ClientRedisRepository() {
        super(Client.class, "client:");
    }

}
