package com.eshop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eshop.common.entity.Role;
import com.eshop.common.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<User> listALl() {
		return (List<User>) userRepository.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepository.findAll();
	}
	
	public void save(User user) {
		boolean existingUser = user.getId()!=null;

		if(existingUser) {
			Optional<User> user1 = userRepository.findById(user.getId());

			if(user.getPassword().isEmpty()) {
				user.setPassword(user1.get().getPassword());
			} else {
				encodePass(user);
			}
		} else {
			encodePass(user);
		}
		userRepository.save(user);
	}
	
	private void encodePass(User user) {
		String encodePass = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePass);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User user = userRepository.getUserByEmail(email);
		if (user == null) return true;
		boolean isCreatingNew = (id == null);
		if(isCreatingNew) {
			if(user != null) return false;
		}else {
			if(user.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public Optional<User> getUserById(Integer id) throws UserNotFoundException {
		try{
			return userRepository.findById(id);
		}catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not find any user by id = " + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException {
		Long count = userRepository.countById(id);

		if(count==0 || count == null) {
			throw new UserNotFoundException("Could not find any user by id = " + id);
		}
		userRepository.deleteById(id);
	}

	public void updateUserEnabledStatus(Integer id , boolean enabled) {
		userRepository.updateEnableStatus(id, enabled);
	}
}
