package library.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import library.managers.BookManager;
import library.managers.ClientManager;
import library.managers.RentManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class mainTest {

    ClientRepository clientRepository = new ClientRepository();

    private ClientManager clientManager;

    private BookManager bookManager;

    private RentManager rentManager;

    @BeforeEach
    void beforeEach() {
        this.clientManager = new ClientManager();
        this.bookManager = new BookManager();
        this.rentManager = new RentManager();
    }
    @Test
    void addSameObjects() throws Exception {
        bookManager.registerBook("s","s","123213","3213");
        clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        var xd = clientManager.getClient("231312341");
        clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);

    }

   /* @Test
    void addSameObjects() throws Exception {

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
    void addCheckRegister() throws Exception {
        ;
        var client1 = clientManager.registerClient("imie", "naziwsko", "231312341", 45);
        var client2 = clientManager.registerClient("dziecko", "naziwsko", "2313123341", 13);
        var book1 = bookManager.registerBook("najlepssza ksiega", "najlepszy autor", "2131341", "genre");
        var rent1 = rentManager.rentBook("2313123341", "2131341");
        assertEquals(clientManager.getClient("231312341"), client1);
        assertEquals(clientManager.getClient("2313123341"), client2);
        assertEquals(bookManager.getBook("2131341"), book1);
        assertEquals(rentManager.getRentByBook("2131341"), rent1);
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
        assertTrue(clientManager.getClient("231312341").isArchive());
        assertTrue(bookManager.getBook("21313412").isArchive());
        rentManager.rentBook("2313123341", "21313413");
        assertThrows(Exception.class, () -> {
            clientManager.unregisterClient("2313123341");
        });
        assertThrows(Exception.class, () -> {
            bookManager.unregisterBook("21313413");
        });
    }*/


}
