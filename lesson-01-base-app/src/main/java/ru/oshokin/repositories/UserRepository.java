package ru.oshokin.repositories;

import ru.oshokin.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUserName(String userName);
}
