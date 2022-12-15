package library.repository.redis;

import library.model.Book;
import library.model.Client;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;

import javax.jms.Message;
import java.text.MessageFormat;
import java.util.Optional;


public class BookRedisRepository extends AbstractRedisRepository<Book>{
    public BookRedisRepository() {
        super(Book.class, "book:");
    }
}
