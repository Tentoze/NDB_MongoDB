package library.managers;

import library.model.Book;
import library.repository.BookRepository;
import library.repository.RentRepository;
import library.repository.mongo.BookMongoRepository;
import library.repository.mongo.RentMongoRepository;
import library.repository.redis.BookRedisRepository;
import library.repository.redis.RentRedisRepository;


import java.util.List;

public class BookManager {
    private BookRepository bookRepository;
    private RentRepository rentRepository;

    public BookManager() {
        this.rentRepository = new RentRepository(new RentMongoRepository(), new RentRedisRepository());
        this.bookRepository = new BookRepository(new BookRedisRepository(),new BookMongoRepository());
    }

    public Book getBook(String serialNumber) {
        return bookRepository.getBySerialNumber(serialNumber).orElse(null);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book registerBook(String title, String author, String serialNumber, String genre) {
        Book book = new Book(title, author, serialNumber, genre);
        if(bookRepository.getBySerialNumber(serialNumber).orElse(null) == null){
            bookRepository.add(book);
        } else {
            throw new RuntimeException("Ta ksiazka została istnieje juz w bazie danych");
        }
        return book;
    }

    public void unregisterBook(String serialNumber) throws Exception {
        Book book = bookRepository.getBySerialNumber(serialNumber).orElseThrow(() -> new Exception("There is no book with that SerialNumber"));
        if (rentRepository.findByBook(book) != null) {
            throw new Exception("This book is already rented");
        }
        book.setArchived(true);
        bookRepository.update(book);
    }

}


