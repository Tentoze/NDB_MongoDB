package library.managers;

import library.model.Book;
import library.model.Client;
import library.model.Rent;
import library.repository.BookRepository;
import library.repository.ClientRepository;
import library.repository.RentRepository;


import java.util.Date;
import java.util.List;

public class RentManager {

    private RentRepository rentRepository;
    private ClientRepository clientRepository;
    private BookRepository bookRepository;

    public RentManager() {
        this.rentRepository = new RentRepository();
        this.clientRepository = new ClientRepository();
        this.bookRepository = new BookRepository();
    }

    public Rent rentBook(String personalID, String serialNumber) throws Exception {
        try {
            Client client = clientRepository.findByPersonalID(personalID);
            Book book = bookRepository.findBySerialNumber(serialNumber);
            checkIfBookCanBeRented(client, book);
            bookRepository.incrementIsRented(book);
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

    public void returnBook(String serialNumber) {
        try {
            Book book = bookRepository.findBySerialNumber(serialNumber);
            if (book == null) {
                throw new Exception("There is no book with that serial number");
            }
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
        return rentRepository.findByBook(bookRepository.findBySerialNumber(serialnumber));
    }

    public List<Rent> getRentByClient(String personalID){
        return rentRepository.findByClient(clientRepository.findByPersonalID(personalID));
    }




}
