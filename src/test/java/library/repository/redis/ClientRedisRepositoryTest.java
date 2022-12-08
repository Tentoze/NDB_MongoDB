package library.repository.redis;

import library.model.Book;
import library.model.Child;
import library.model.Client;
import library.model.Rent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientRedisRepositoryTest {

    ClientRedisRepository clientRedisRepository;
    @BeforeEach
    void setUp() {
        clientRedisRepository = new ClientRedisRepository();
        clientRedisRepository.clearCache();
    }
    @Test
    void addClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        clientRedisRepository.add(client);
        assertEquals(clientRedisRepository.getById(client.getEntityId().getUUID()).get().toString(), client.toString());
    }

    @Test
    void deleteClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        clientRedisRepository.add(client);
        clientRedisRepository.delete(client.getEntityId().getUUID());
        assertThrows(Exception.class, () -> {
            clientRedisRepository.getById(client.getEntityId().getUUID());
        });
    }

    @Test
    void updateClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        clientRedisRepository.add(client);
        client.setLastName("zmiana");
        clientRedisRepository.update(client);
        assertEquals(clientRedisRepository.getById(client.getEntityId().getUUID()).get().toString(), client.toString());
    }

    @Test
    void listAllClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Client client1 = new Child("imie2", "naziwsko2", "2313123412", 45);
        clientRedisRepository.add(client);
        clientRedisRepository.add(client1);
        List<Client> clients = new ArrayList<>();
        clients.add(client);
        clients.add(client1);
        assertEquals(clients.size(),clientRedisRepository.findAll().size());
    }

    @Test
    void redisClearCacheTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Client client1 = new Child("imie2", "naziwsko2", "2313123412", 45);
        clientRedisRepository.add(client);
        clientRedisRepository.add(client1);
        assertEquals(2,clientRedisRepository.size());
        clientRedisRepository.clearCache();
        assertEquals(0,clientRedisRepository.size());
    }
}