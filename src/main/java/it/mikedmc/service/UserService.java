package it.mikedmc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.mikedmc.model.Role;
import it.mikedmc.model.User;
import it.mikedmc.model.UserRole;
import it.mikedmc.repository.RoleRepository;
import it.mikedmc.repository.UserRepository;
import it.mikedmc.repository.UserRoleRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        // Crittografa la password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Salva l'utente
        userRepository.save(user);
    }
    
    public User findFromEmail (String email) {
    	return userRepository.findByEmail(email);
    }
    
    public boolean userUsernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean userEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    
    @Transactional
    public User getUserWithRoles(String username) {
        return userRepository.findByUsernameOrEmailAndConfirmed(username);
    }

	public void update(User user) {
		userRepository.save(user);
	}
	
	public int getCoutUserConfirmed () {
        return (int) userRepository.countByConfirmed(true);
	}
	
	public int getCountUserNotConfirmed () {
        return (int) userRepository.countByConfirmed(false);
	}
	public int getCount() {
		return (int) userRepository.count();
	}
	
	public long getUserCountCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.countUsersCreatedBetween(startDate, endDate);
    }
	
	public List<User> getUsersCreatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.findUsersCreatedBetween(startDate, endDate);
    }
	
	public long getActiveUserCount(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.countActiveUsers(startDate, endDate);
    }
	
	public List<User> getActiveUsers(LocalDateTime startDate, LocalDateTime endDate) {
        return userRepository.findActiveUsers(startDate, endDate);
    }
	
}