package ru.oshokin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.oshokin.entities.Role;
import ru.oshokin.entities.SystemUser;
import ru.oshokin.entities.User;
import ru.oshokin.repositories.RoleRepository;
import ru.oshokin.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Autowired
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public User findByUserName(String userName) {
		return userRepository.findOneByUserName(userName);
	}

	@Override
	@Transactional
	public User findById(Long id) {
		return userRepository.findOneById(id);
	}

	@Override
	@Transactional
	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	@Transactional
	public void createNewUser(SystemUser systemUser) {
		User user = new User();
		user.setUserName(systemUser.getUserName());
		user.setPassword(passwordEncoder.encode(systemUser.getPassword()));
		user.setFirstName(systemUser.getFirstName());
		user.setLastName(systemUser.getLastName());
		user.setEmail(systemUser.getEmail());

		user.setRoles(Arrays.asList(roleRepository.findOneByName("ROLE_EMPLOYEE")));

		userRepository.save(user);
	}

	@Override
	@Transactional
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findOneByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
