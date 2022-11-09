package library.repository;


import library.model.Book;

public class BookRepository extends AbstractMongoRepository {


    public BookRepository() {
        super("books", Book.class);
    }
}
