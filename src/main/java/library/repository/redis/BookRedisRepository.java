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
        Schema schema = new Schema().addTextField("$.serialNumber",1.0);
        IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.JSON).setPrefixes(new String[]{prefix});
        try {
            pool.ftDropIndex("essa");
        } catch (JedisDataException jde) {
            System.out.println(jde);
        } finally {
            pool.ftCreate("essa", IndexOptions.defaultOptions().setDefinition(rule),schema);
        }
    }
    public Book findBySerialNumber(String serialNumber) {
        MessageFormat format = new MessageFormat("@\\$\\.serialNumber:{0}");
        Query query = new Query(format.format(new Object[]{serialNumber}));
        try {
            SearchResult searchResult = pool.ftSearch("essa",query);
            searchResult.getDocuments().get(0).get("$");
        } catch (Exception e) {

        }
        return null;
    }
}
