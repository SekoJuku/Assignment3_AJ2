package kz.edu.astanait.application.services.interfaces;

import kz.edu.astanait.application.models.User;

import java.util.List;

public interface IUserService {
    boolean register(User user);
    User findByUsername(String username);
    List<User> getAll();
    boolean deleteById(Long id);
    User findById(Long id);
    void changePassword(String newPassword, Long userID);
    boolean checkPasswordEquality(String hashedPassword, String password);
}
