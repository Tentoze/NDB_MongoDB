package library.repository.redis;


import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import library.model.AbstractEntity;
import library.repository.RepositoryInterface;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class AbstractRedisRepository<T extends AbstractEntity> implements RepositoryInterface<T> {

    protected static JedisPooled pool;
    protected static Jsonb jsonb = JsonbBuilder.create();
    protected Class<T> clazz;
    protected String prefix;
    InputStream inputStream;


    Properties prop = new Properties();

    public void initConnection() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        try {
            Properties prop = new Properties();
            String propFileName = "redis.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null) {
                prop.load(inputStream);
            }
            pool = new JedisPooled(new HostAndPort(prop.getProperty("redis.host"), Integer.parseInt(prop.getProperty("redis.port"))), clientConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractRedisRepository(Class<T> clazz, String prefix) {
        this.clazz = clazz;
        this.prefix = prefix;
        initConnection();

    }

    public boolean checkConnection() {
        try {
            pool.getPool().getResource().isConnected();
            return true;
        } catch (JedisException e) {
            return false;
        }
    }

    public void clearCache() {
        Set<String> keys = pool.keys("*");
        for (String key : keys) {
            pool.del(key);
        }
    }

    public Optional<T> getById(UUID id) {
        Optional<String> found = Optional.of(pool.get(prefix + id.toString()));
        Optional<T> d =  Optional.of(jsonb.fromJson(found.get(), this.clazz));
        return Optional.ofNullable(d.stream().filter(i -> i.getEntityId().getUUID() != null).findFirst().orElse(null));
    }

    @Override
    public void delete(UUID id) {
        pool.del(prefix + id);
    }

    public T add(T entity) {
        String entityStr = jsonb.toJson(entity);
        pool.set(prefix + entity.getEntityId().getUUID().toString(), entityStr);
        return entity;
    }

    public void remove(T entity) {
        pool.del(prefix + entity.getEntityId().getUUID().toString());
    }

    public void update(T updatedEntity) {
        String entityStr = jsonb.toJson(updatedEntity);
        pool.set(prefix + updatedEntity.getEntityId().getUUID().toString(), entityStr);
    }

    public long size() {
        return findAll().size();
    }

    public List<T> findAll() {
        List<T> all = new ArrayList<>();
        Set<String> keys = pool.keys(prefix + "*");
        for (String key : keys) {
            all.add(jsonb.fromJson(pool.get(key), this.clazz));
        }
        return all;
    }

    public void close() throws Exception {
        System.out.println("closing redis connection");
        pool.getPool().destroy();
        pool.close();
    }
}
