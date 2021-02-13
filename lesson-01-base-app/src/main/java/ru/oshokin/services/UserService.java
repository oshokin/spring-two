package ru.oshokin.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.oshokin.entities.SystemUser;
import ru.oshokin.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUserName(String userName);
    User findById(Long id);
    List<User> getAllUsers();
    void createNewUser(SystemUser systemUser);
    void save(User user);

}
