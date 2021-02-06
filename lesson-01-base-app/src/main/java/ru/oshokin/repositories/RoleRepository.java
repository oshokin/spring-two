package ru.oshokin.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.oshokin.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findOneByName(String theRoleName);

}
