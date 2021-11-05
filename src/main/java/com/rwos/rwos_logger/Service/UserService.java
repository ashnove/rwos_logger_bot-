package com.rwos.rwos_logger.Service;

import java.util.List;
import java.util.Objects;

import com.rwos.rwos_logger.Entity.Users;
import com.rwos.rwos_logger.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Users> getAllStatus() {
        return userRepository.findAll();
    }

    public void check(Users user) {
        try {
            if (Objects.isNull(userRepository.findByUserName(user.getUserName())))
                userRepository.save(user);
        } catch (Exception e) {
            System.out.println("check() | " + e.getMessage());
        }
    }
}
