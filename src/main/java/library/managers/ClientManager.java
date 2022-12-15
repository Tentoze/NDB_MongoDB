package library.managers;

import library.model.Adult;
import library.model.Child;
import library.model.Client;
import library.repository.ClientRepository;
import library.repository.RentRepository;
import library.repository.mongo.ClientMongoRepository;
import library.repository.mongo.RentMongoRepository;
import library.repository.redis.ClientRedisRepository;
import library.repository.redis.RentRedisRepository;

import java.util.List;

public class ClientManager {
    private RentRepository rentRepository;
    private ClientRepository clientRepository;

    public ClientManager() {
        this.rentRepository = new RentRepository(new RentMongoRepository(), new RentRedisRepository());
        this.clientRepository = new ClientRepository(new ClientRedisRepository(),new ClientMongoRepository());
    }

    public Client getClient(String personalID) {
        return clientRepository.getByPersonalID(personalID);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client registerClient(String firstname, String lastname, String personalID, int age) {
        Client client;
        if(clientRepository.getByPersonalID(personalID) == null){
            if (age < 18) {
                client = new Child(firstname, lastname, personalID, age);
                clientRepository.add(client);
            } else {
                client = new Adult(firstname, lastname, personalID, age);
                clientRepository.add(client);
            }
        } else {
            throw new RuntimeException("Ten pesel znajduje sie juz w bazie danych");
        }

        return client;
    }

    public void unregisterClient(String personalID) throws Exception {
        Client client = clientRepository.getByPersonalID(personalID);
        if (client.getDebt() > 0) {
            throw new Exception("Client have to pay debt firstly");
        }
        if (!rentRepository.findByClient(client).isEmpty()) {
            throw new Exception("Client have to pay return books firstly");
        }
        client.setArchived(true);
        clientRepository.update(client);
    }

    public void payDebt(String personalID, Float amount) throws Exception {
        Client client = clientRepository.getByPersonalID(personalID);
      //  clientRepository.updateDebtByPersonalID(personalID, client.getDebt() - amount);
    }
    public Float getDebt(String personalID) throws Exception {
        Client client = clientRepository.getByPersonalID(personalID);
        return client.getDebt();
    }
}
