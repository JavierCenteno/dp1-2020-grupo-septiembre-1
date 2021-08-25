package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.AuthoritiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class AuthoritiesService {

	////////////////////////////////////////////////////////////////////////////////
	// Repository

	private AuthoritiesRepository authoritiesRepository;

	////////////////////////////////////////////////////////////////////////////////
	// Services

	private UserService userService;

	////////////////////////////////////////////////////////////////////////////////
	// Constructor

	@Autowired
	public AuthoritiesService(AuthoritiesRepository authoritiesRepository, UserService userService) {
		this.authoritiesRepository = authoritiesRepository;
		this.userService = userService;
	}

	////////////////////////////////////////////////////////////////////////////////
	// Methods

	@Transactional
	public void saveAuthorities(Authorities authorities) {
		authoritiesRepository.save(authorities);
	}

	@Transactional
	public void saveAuthorities(String username, String role) {
		Authorities authority = new Authorities();
		Optional<User> user = userService.findUser(username);
		if (user.isPresent()) {
			authority.setUser(user.get());
			authority.setAuthority(role);
			// user.get().getAuthorities().add(authority);
			authoritiesRepository.save(authority);
		} else {
			throw new DataAccessException("User '" + username + "' not found!") {
				private static final long serialVersionUID = 1L;
			};
		}
	}

}
