package com.rwos.rwos_logger.Service;

import java.util.List;
import java.util.Objects;

import com.rwos.rwos_logger.Entity.User;
import com.rwos.rwos_logger.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllStatus() {
        return userRepository.findAll();
    }

    public void check(User user) {
        try {
            if (Objects.isNull(userRepository.findByUserId(user.getUserId())))
                userRepository.save(user);
            else {
                String newStatus = user.getStatus();
                String newDate = user.getDateLog();
                String newTime = user.getTimeLog();
                Long userId = user.getUserId();
                userRepository.updateUserStatus(newDate, newStatus, newTime, userId);
            }
        } catch (Exception e) {
            System.out.println("check() | " + e.getMessage());
        }
    }
}
