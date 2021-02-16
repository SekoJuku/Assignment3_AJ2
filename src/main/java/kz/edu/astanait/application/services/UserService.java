package kz.edu.astanait.application.services;

import kz.edu.astanait.application.loggers.RegistrationLogger;
import kz.edu.astanait.application.models.User;
import kz.edu.astanait.application.repositories.RoleRepository;
import kz.edu.astanait.application.repositories.UserRepository;
import kz.edu.astanait.application.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(User user) {
        if(userRepository.existsByUsername(user.getUsername())){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.getOne(2L));

        userRepository.save(user);

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new RegistrationLogger(user));
        executorService.shutdown();
        return true;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteById(Long id) {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public void changePassword(String newPassword, Long userID) {
        newPassword = passwordEncoder.encode(newPassword);
        userRepository.changePassword(newPassword, userID);
    }

    @Override
    public boolean checkPasswordEquality(String hashedPassword, String password) {
        return passwordEncoder.matches(password,hashedPassword);
    }
}
