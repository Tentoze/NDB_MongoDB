package library.managers;

import jakarta.persistence.EntityManager;
import library.model.Book;
import library.repository.BookRepository;


import java.util.List;

public class BookManager {
    BookRepository bookRepository;

    public BookManager() {
        this.bookRepository = new BookRepository();
    }

    /*public Book getBook(String serialNumber) {
        return bookRepository.findBySerialNumber(serialNumber);
    }*/

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public void registerBook(String title, String author, String serialNumber, String genre) {
        bookRepository.add(new Book(title, author, serialNumber, genre));
    }

/*    public void unregisterBook(String serialNumber) throws Exception {
        Book book = bookRepository.findBySerialNumber(serialNumber);
        if (rentRepository.existsByBook(book)) {
            throw new Exception("This book is already rented");
        }
        book.setArchive(true);
        bookRepository.update(book);
    }*/

}


