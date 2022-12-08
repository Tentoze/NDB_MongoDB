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

class RentRedisRepositoryTest {

    RentRedisRepository rentRedisRepository;
    @BeforeEach
    void setUp() {
        rentRedisRepository = new RentRedisRepository();
        rentRedisRepository.clearCache();
    }
    @Test
    void addClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Rent rent = new Rent(client, book);
        rentRedisRepository.add(rent);
        assertEquals(rentRedisRepository.getById(rent.getEntityId().getUUID()).get().toString(), rent.toString());
    }

    @Test
    void deleteClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Rent rent = new Rent(client, book);
        rentRedisRepository.add(rent);
        rentRedisRepository.delete(rent.getEntityId().getUUID());
        assertThrows(Exception.class, () -> {
            rentRedisRepository.getById(rent.getEntityId().getUUID());
        });
    }

    @Test
    void updateClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Rent rent = new Rent(client, book);
        Client client2 = new Child("imie2", "naziwsko2", "2313123421", 45);
        rentRedisRepository.add(rent);
        rent.setClient(client2);
        assertNotEquals(rentRedisRepository.getById(rent.getEntityId().getUUID()).get().toString(), rent.toString());
        rentRedisRepository.update(rent);
        assertEquals(rentRedisRepository.getById(rent.getEntityId().getUUID()).get().toString(), rent.toString());
    }

    @Test
    void listAllClientTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Client client1 = new Child("imie2", "naziwsko2", "2313123412", 45);
        Book book1 = new Book("najlepssza ksiega21", "najlepszy autor21", "213134122", "genre");
        Rent rent = new Rent(client, book);
        Rent rent1 = new Rent(client1, book1);
        rentRedisRepository.add(rent);
        rentRedisRepository.add(rent1);
        List<Rent> rents = new ArrayList<>();
        rents.add(rent);
        rents.add(rent1);
        assertEquals(rents.size(), rentRedisRepository.findAll().size());
    }

    @Test
    void redisClearCacheTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Client client1 = new Child("imie2", "naziwsko2", "2313123412", 45);
        Book book1 = new Book("najlepssza ksiega21", "najlepszy autor21", "213134122", "genre");
        Rent rent = new Rent(client, book);
        Rent rent1 = new Rent(client1, book1);
        rentRedisRepository.add(rent);
        rentRedisRepository.add(rent1);
        assertEquals(2, rentRedisRepository.size());
        rentRedisRepository.clearCache();
        assertEquals(0, rentRedisRepository.size());
    }
}