package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    UserService userService;


    @PostMapping("/users/check-email")
    public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {

        return userService.isEmailUnique(id, email) ? "OK" : "Duplicate";

    }
}
