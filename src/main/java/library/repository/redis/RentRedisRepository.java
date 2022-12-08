package library.repository.redis;

import library.model.Rent;

public class RentRedisRepository extends AbstractRedisRepository<Rent> {
    public RentRedisRepository() {
        super(Rent.class, "rent:");
    }

}
