package library.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import library.model.Book;
import library.model.Client;
import library.model.Rent;
import library.repository.kafka.Consumers;
import library.repository.kafka.Producer;
import library.repository.kafka.Topics;
import library.repository.mongo.RentMongoRepository;
import library.repository.redis.RentRedisRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class RentRepository implements RepositoryInterface<Rent> {

    private RentMongoRepository rentMongoRepository;
    private RentRedisRepository rentRedisRepository;
    private Producer producer;

    public RentRepository(RentMongoRepository rentMongoRepository, RentRedisRepository rentRedisRepository) {
        this.rentMongoRepository = rentMongoRepository;
        this.rentRedisRepository = rentRedisRepository;
        try {
            Topics.RENT_TOPIC.createTopic();
            this.producer = new Producer();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Rent findByBook(Book book) {
        return rentMongoRepository.findByBook(book);
    }

    public List<Rent> findByClient(Client client) {
        return rentMongoRepository.findByClient(client);
    }

    @Override
    public Rent add(Rent entity) {

        rentRedisRepository.add(entity);
        rentMongoRepository.add(entity);
        try {
            producer.send(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public Optional<Rent> getById(UUID id) {
        Rent rent = null;
        if (rentRedisRepository.checkConnection()) {
            rent = rentRedisRepository.getById(id).orElse(null);
        }
        if (rent == null) {
            return Optional.ofNullable(rentMongoRepository.findById(id));
        }
        return Optional.of(rent);
    }

    @Override
    public void delete(UUID id) {
        rentRedisRepository.delete(id);
        rentMongoRepository.delete(id);
    }

    @Override
    public void update(Rent entity) {
        rentRedisRepository.update(entity);
        rentMongoRepository.update(entity);
    }

    @Override
    public long size() {
        return findAll().size();
    }

    @Override
    public List<Rent> findAll() {
        List<Rent> rents = new ArrayList<>();
        if (rentRedisRepository.checkConnection()) {
            List<Rent> found = rentRedisRepository.findAll();
            rents.addAll(found);
        } else {
            List<Rent> found = rentMongoRepository.findAll();
            rents.addAll(found);
        }
        return rents;
    }
}
