package ru.oshokin.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.oshokin.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findOneByUserName(String userName);
    User findOneById(Long id);

}
