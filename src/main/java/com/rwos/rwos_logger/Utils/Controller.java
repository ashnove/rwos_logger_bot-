package com.rwos.rwos_logger.Utils;

import com.rwos.rwos_logger.Entity.Users;
import com.rwos.rwos_logger.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Controller {

    @Autowired
    private UserService userService;

    public void userCheck(Users user) {
        try {
            userService.check(user);
        } catch (Exception e) {
            System.out.println("lol");
        }
    }
}
