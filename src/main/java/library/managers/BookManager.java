package library.managers;

import library.model.Book;
import library.repository.BookRepository;
import library.repository.RentRepository;


import java.util.List;

public class BookManager {
    BookRepository bookRepository;
    RentRepository rentRepository;

    public BookManager() {
        this.bookRepository = new BookRepository();
        this.rentRepository = new RentRepository();
    }

    public Book getBook(String serialNumber) {
        return bookRepository.findBySerialNumber(serialNumber);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book registerBook(String title, String author, String serialNumber, String genre) {
        Book book = new Book(title, author, serialNumber, genre);
        if(bookRepository.findBySerialNumber(serialNumber) == null){
            bookRepository.add(book);
        } else {
            throw new RuntimeException("Ta ksiazka została istnieje juz w bazie danych");
        }
        return book;
    }

    public void unregisterBook(String serialNumber) throws Exception {
        Book book = bookRepository.findBySerialNumber(serialNumber);
        if (rentRepository.findByBook(book) != null) {
            throw new Exception("This book is already rented");
        }
        book.setArchived(true);
        bookRepository.update(book);
    }

}


