package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class UserService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private UserRepository userRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Password encoder

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional
	public void saveUser(User user) throws DataAccessException {
		user.setEnabled(true);
		userRepository.save(user);
	}

	public Optional<User> findUser(String username) {
		return userRepository.findById(username);
	}

	public Optional<User> findPrincipal() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return this.findUser(username);
	}

}
