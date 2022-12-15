package library.repository;

import library.model.Client;
import library.repository.mongo.ClientMongoRepository;
import library.repository.redis.ClientRedisRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepository implements RepositoryInterface<Client> {
    private ClientRedisRepository clientRedisRepository;
    private ClientMongoRepository clientMongoRepository;

    public ClientRepository(ClientRedisRepository clientRedisRepository, ClientMongoRepository clientMongoRepository) {
        this.clientRedisRepository = clientRedisRepository;
        this.clientMongoRepository = clientMongoRepository;
    }

    @Override
    public Client add(Client entity) {
        clientRedisRepository.add(entity);
        clientMongoRepository.add(entity);
        return entity;
    }

    @Override
    public Optional<Client> getById(UUID id) {
        Client client = null;
        if (clientRedisRepository.checkConnection()) {
            client = clientRedisRepository.getById(id).orElse(null);
        }
        if (client == null) {
            return Optional.ofNullable(clientMongoRepository.findById(id));
        }
        return Optional.of(client);
    }


    public Client getByPersonalID(String personalID) {
        return clientMongoRepository.findByPersonalID(personalID);
    }

    @Override
    public void delete(UUID id) {
        clientRedisRepository.delete(id);
        clientMongoRepository.delete(id);
    }

    @Override
    public void update(Client entity) {
        clientRedisRepository.update(entity);
        clientMongoRepository.update(entity);
    }

    @Override
    public long size() {
        return findAll().size();
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        if (clientRedisRepository.checkConnection()) {
            List<Client> found = clientRedisRepository.findAll();
            clients.addAll(found);
        } else {
            List<Client> found = clientMongoRepository.findAll();
            clients.addAll(found);
        }
        return clients;
    }
}
