package ru.oshokin.store.services;


import ru.oshokin.store.entities.SystemUser;
import ru.oshokin.store.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);
    boolean save(SystemUser systemUser);
}
