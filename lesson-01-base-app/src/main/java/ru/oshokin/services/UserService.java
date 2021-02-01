package ru.oshokin.services;

import ru.oshokin.entities.SystemUser;
import ru.oshokin.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUserName(String userName);
    void save(SystemUser systemUser);
}
