package library.managers;

import library.model.Book;
import library.repository.BookRepository;
import library.repository.RentRepository;
import library.repository.mongo.BookMongoRepository;
import library.repository.mongo.RentMongoRepository;
import library.repository.redis.BookRedisRepository;
import library.repository.redis.RentRedisRepository;


import java.util.List;
import java.util.UUID;

public class BookManager {
    private BookRepository bookRepository;
    private RentRepository rentRepository;

    public BookManager() {
        this.rentRepository = new RentRepository(new RentMongoRepository(), new RentRedisRepository());
        this.bookRepository = new BookRepository(new BookRedisRepository(),new BookMongoRepository());
    }

    public Book getBook(String serialNumber) {
        return bookRepository.getBySerialNumber(serialNumber);
    }
    public Book getBook(UUID bookID) {
        return bookRepository.getById(bookID).orElse(null);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book registerBook(String title, String author, String serialNumber, String genre) {
        Book book = new Book(title, author, serialNumber, genre);
        if(bookRepository.getBySerialNumber(serialNumber) == null){
            bookRepository.add(book);
        } else {
            throw new RuntimeException("Ta ksiazka została istnieje juz w bazie danych");
        }
        return book;
    }

    public void unregisterBook(String serialNumber) throws Exception {
        Book book = bookRepository.getBySerialNumber(serialNumber);
        if (rentRepository.findByBook(book) != null) {
            throw new Exception("This book is already rented");
        }
        book.setArchived(true);
        bookRepository.update(book);
    }

}


