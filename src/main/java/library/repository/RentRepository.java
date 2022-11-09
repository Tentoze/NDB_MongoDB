package library.repository;

import jakarta.persistence.EntityManager;
import library.model.Rent;

public class RentRepository extends AbstractMongoRepository {


    public RentRepository() {
        super("rents", Rent.class);
    }
}
