package ru.oshokin.store.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.oshokin.store.entities.SystemUser;
import ru.oshokin.store.entities.User;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);
    boolean save(SystemUser systemUser);
}
