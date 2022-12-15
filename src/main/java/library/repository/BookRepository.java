package library.repository;

import library.model.Book;
import library.model.Client;
import library.repository.mongo.BookMongoRepository;
import library.repository.redis.BookRedisRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookRepository implements RepositoryInterface<Book> {
    private BookRedisRepository bookRedisRepository;
    private BookMongoRepository bookMongoRepository;

    public BookRepository(BookRedisRepository bookRedisRepository, BookMongoRepository bookMongoRepository) {
        this.bookRedisRepository = bookRedisRepository;
        this.bookMongoRepository = bookMongoRepository;
    }

    @Override
    public Book add(Book entity) {
        bookRedisRepository.add(entity);
        bookMongoRepository.add(entity);
        return entity;
    }

    @Override
    public Optional<Book> getById(UUID id) {
        Book book = null;
        if (bookRedisRepository.checkConnection()) {
            book = bookRedisRepository.getById(id).orElse(null);
        }
        if (book == null) {
            return Optional.ofNullable(bookMongoRepository.findById(id));
        }
        return Optional.of(book);
    }

    @Override
    public void delete(UUID id) {
        bookMongoRepository.delete(id);
        bookRedisRepository.delete(id);
    }

    @Override
    public void update(Book entity) {
        bookRedisRepository.update(entity);
        bookMongoRepository.update(entity);
    }

    @Override
    public long size() {
        return findAll().size();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        if (bookRedisRepository.checkConnection()) {
            List<Book> found = bookRedisRepository.findAll();
            books.addAll(found);
        } else {
            List<Book> found = bookMongoRepository.findAll();
            books.addAll(found);
        }
        return books;
    }

    public Book getBySerialNumber(String serialNumber) {
        return bookMongoRepository.findBySerialNumber(serialNumber);
    }
}

