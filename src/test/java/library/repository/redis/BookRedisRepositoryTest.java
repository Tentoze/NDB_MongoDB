package library.repository.redis;

import library.model.Book;
import library.model.Child;
import library.model.Client;
import library.model.Rent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookRedisRepositoryTest {

    private BookRedisRepository bookRedisRepository;

    Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");


    @BeforeEach
    void setUp() {
        this.bookRedisRepository = new BookRedisRepository();
        bookRedisRepository.clearCache();
    }

    @Test
    void addBookTest() {
        Client client = new Child("imie", "naziwsko", "231312341", 45);
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Rent rent = new Rent(client, book);
        bookRedisRepository.add(book);
        assertEquals(bookRedisRepository.getById(book.getEntityId().getUUID()).get().toString(), book.toString());
    }

    @Test
    void deleteBookTest() {
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        bookRedisRepository.add(book);
        bookRedisRepository.delete(book.getEntityId().getUUID());
        assertThrows(Exception.class, () -> {
            bookRedisRepository.getById(book.getEntityId().getUUID());
        });
    }

    @Test
    void updateBookTest() {
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        bookRedisRepository.add(book);
        book.setTitle("zmiana");
        bookRedisRepository.update(book);
        assertEquals(bookRedisRepository.getById(book.getEntityId().getUUID()).get().toString(), book.toString());
    }

    @Test
    void listAllBookTest() {
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Book book1 = new Book("najlepssza ksiega2", "najlepszy autor2", "213134123", "genre");
        bookRedisRepository.add(book);
        bookRedisRepository.add(book1);
        List<Book> books = new ArrayList<>();
        books.add(book);
        books.add(book1);
        assertEquals(books.size(),bookRedisRepository.findAll().size());
    }

    @Test
    void redisClearCacheTest() {
        Book book = new Book("najlepssza ksiega1", "najlepszy autor1", "21313412", "genre");
        Book book1 = new Book("najlepssza ksiega2", "najlepszy autor2", "213134123", "genre");
        bookRedisRepository.add(book);
        bookRedisRepository.add(book1);
        assertEquals(2,bookRedisRepository.size());
        bookRedisRepository.clearCache();
        assertEquals(0,bookRedisRepository.size());
    }
}