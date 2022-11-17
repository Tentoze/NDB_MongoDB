package library.repository;

import jakarta.persistence.OptimisticLockException;
import library.managers.BookManager;
import library.managers.ClientManager;
import library.managers.RentManager;
import library.model.Book;
import library.model.Client;
import library.model.Rent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class mainTest {



    private ClientManager clientManager;
    private BookManager bookManager;
    private RentManager rentManager;
    private BookRepository bookRepository;
    private RentRepository rentRepository;
    private ClientRepository clientRepository;


    @BeforeAll
    void beforeAll() {
        this.bookRepository =  new BookRepository();
        this.clientRepository = new ClientRepository();
        this.rentRepository = new RentRepository();
    }
    @BeforeEach
    void beforeEach() {
        this.clientManager = new ClientManager();
        this.bookManager = new BookManager();
        this.rentManager = new RentManager();
        clientRepository.clearDatabase();
        rentRepository.clearDatabase();
        bookRepository.clearDatabase();
    }
    @Test
    void testCRUD() throws Exception {
        var client1 = clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        var client2 = clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        var book1 = bookManager.registerBook("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        var book2 = bookManager.registerBook("najlepssza ksiega2", "najlepszy autor2", "21313413", "genre");
        rentManager.rentBook("2313123341", "21313412");
        rentManager.rentBook("2313123341", "21313413");

        assertEquals(clientRepository.findByPersonalID("231312341").toString(), client1.toString());
        client1.setAge(50);
        clientRepository.update(client1);
        assertEquals(clientRepository.findByPersonalID("231312341").getAge(),50);
        clientRepository.delete(client1.getEntityId().getUUID());
        assertNull(clientRepository.findByPersonalID("231312341"));

    }

    @Test
    void addSameObjects2() throws Exception {

        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);

        bookManager.registerBook("najlepssza ksiega", "najlepszy autor", "2131341", "genre");
        rentManager.rentBook("2313123341", "2131341");
        assertThrows(Exception.class, () -> {
            rentManager.rentBook("2313123341", "2131341");
        });
        assertThrows(Exception.class, () -> {
            bookManager.registerBook("najlepssza ksiega", "najlepszy autor", "2131341", "genre");
        });
        assertThrows(Exception.class, () -> {
            clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        });
    }


    @Test
    void checkTooMuchRents() throws Exception {
        var client1 = clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        var client2 = clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        var book1 = bookManager.registerBook("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        var book2 = bookManager.registerBook("najlepssza ksiega2", "najlepszy autor2", "21313413", "genre");
        var book3 = bookManager.registerBook("najlepssza ksiega3", "najlepszy autor3", "21313414", "genre");
        var book4 = bookManager.registerBook("najlepssza ksiega4", "najlepszy autor4", "21313415", "genre");
        var book5 = bookManager.registerBook("najlepssza ksiega5", "najlepszy autor5", "21313411", "genre");
        var book6 = bookManager.registerBook("najlepssza ksiega5", "najlepszy autor5", "21313416", "genre");
        rentManager.rentBook("2313123341", "21313412");
        rentManager.rentBook("2313123341", "21313413");
        rentManager.rentBook("2313123341", "21313414");
        rentManager.getRentByBook("21313414");
        assertThrows(Exception.class, () -> {
            rentManager.rentBook("2313123341", "21313416");
        });
    }

    @Test
    void checkUnregisterClientAndBook() throws Exception {
        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        bookManager.registerBook("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        bookManager.registerBook("najlepssza ksiega2", "najlepszy autor2", "21313413", "genre");
        clientManager.unregisterClient("231312341");
        bookManager.unregisterBook("21313412");
        assertTrue(clientManager.getClient("231312341").isArchived());
        assertTrue(bookManager.getBook("21313412").isArchived());
        rentManager.rentBook("2313123341", "21313413");
        assertThrows(Exception.class, () -> {
            clientManager.unregisterClient("2313123341");
        });
        assertThrows(Exception.class, () -> {
            bookManager.unregisterBook("21313413");
        });
    }
    @Test
    void concurrentCheckTest() throws Exception {

        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        bookManager.registerBook("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        var client = clientManager.getClient("231312341");
        Client client2 = clientManager.getClient("2313123341");
        Book book = bookManager.getBook("21313412");
        BookManager bookManager1 = new BookManager();
        Book book2 = bookManager1.getBook("21313412");
        Rent rent = new Rent(client,book);
       rentRepository.add(rent);
       bookRepository.incrementIsRented(book);
        bookRepository.incrementIsRented(book2);

    }


}
