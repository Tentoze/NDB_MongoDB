package library.managers;

import library.model.Book;
import library.model.Client;
import library.model.Rent;
import library.repository.BookRepository;
import library.repository.ClientRepository;
import library.repository.RentRepository;
import library.repository.mongo.BookMongoRepository;
import library.repository.mongo.ClientMongoRepository;
import library.repository.mongo.RentMongoRepository;
import library.repository.redis.BookRedisRepository;
import library.repository.redis.ClientRedisRepository;
import library.repository.redis.RentRedisRepository;


import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RentManager {

    private RentRepository rentRepository;
    private ClientRepository clientRepository;
    private BookRepository bookRepository;

    public RentManager() {
        this.rentRepository = new RentRepository(new RentMongoRepository(), new RentRedisRepository());
        this.clientRepository = new ClientRepository(new ClientRedisRepository(),new ClientMongoRepository());
        this.bookRepository = new BookRepository(new BookRedisRepository(),new BookMongoRepository());
    }

    public Rent rentBook(UUID clientID, UUID bookID) throws Exception {
        try {
            Client client = clientRepository.getById(clientID).orElseThrow(() -> new RuntimeException("There is no client with that ID"));
            Book book = bookRepository.getById(bookID).orElseThrow(() -> new RuntimeException("There is no book with that ID"));
            checkIfBookCanBeRented(client, book);
            Rent rent = new Rent(client, book);
            rentRepository.add(rent);
            return rent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkIfBookCanBeRented(Client client, Book book) throws Exception {
        if (client.isArchived()) {
            throw new Exception("Client is archived");
        }
        if (book.isArchived()) {
            throw new Exception("Book is archived");
        }
        if (rentRepository.findByBook(book) != null) {
            throw new Exception("Book is already rented");
        }
        if (client.getMaxBooks() < rentRepository.findByClient(client).size() + 1) {
            throw new Exception("Client have already rented max number of books");
        }
        if (client.getDebt() > 0) {
            throw new Exception("Client have to pay the debt");
        }
    }

    public void returnBook(Book book) {
        try {
            bookRepository.getById(book.getEntityId().getUUID()).orElseThrow(() -> new Exception("There is no book with that serial number"));
            Rent rent = rentRepository.findByBook(book);
            if (rent == null) {
                throw new Exception("There is no rent with this book");
            }
            if(isAfterEndTime(rent)){
                Client client = rent.getClient();
                client.setDebt(client.getDebt() + client.getPenalty());
                clientRepository.update(client);
            }
            rentRepository.delete(rent.getEntityId().getUUID());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAfterEndTime(Rent rent){
        Date date = new Date();
        if(date.after(rent.getEndTime())){
            return true;
        }
        return false;
    }

    public Rent getRentByBook(String serialnumber) {
        return rentRepository.findByBook(bookRepository.getBySerialNumber(serialnumber));
    }

    public List<Rent> getRentByClient(String personalID){
        return rentRepository.findByClient(clientRepository.getByPersonalID(personalID));
    }




}
