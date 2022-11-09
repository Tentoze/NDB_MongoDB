package library.managers;

import library.model.Adult;
import library.model.Child;
import library.model.Client;
import library.repository.ClientRepository;
import library.repository.RentRepository;

import java.util.List;

public class ClientManager {
    private ClientRepository clientRepository;
    private RentRepository rentRepository;

    public ClientManager() {
        this.clientRepository = new ClientRepository();
        this.rentRepository = new RentRepository();
    }

    public Client getClient(String personalID) {
        return clientRepository.findByPersonalID(personalID);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    public Client registerClient(String firstname, String lastname, String personalID, int age) {
        Client client;
        if(clientRepository.findByPersonalID(personalID) == null){
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
        Client client = clientRepository.findByPersonalID(personalID);
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
        Client client = clientRepository.findByPersonalID(personalID);
      //  clientRepository.updateDebtByPersonalID(personalID, client.getDebt() - amount);
    }
    public Float getDebt(String personalID) {
        Client client = clientRepository.findByPersonalID(personalID);
        return client.getDebt();
    }
}
