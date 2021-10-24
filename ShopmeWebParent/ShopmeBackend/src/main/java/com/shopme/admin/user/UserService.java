package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listUsers() {
        return (List<User>) userRepo.findAll();
    }

    public List<Role> listRoles() {

        return (List<Role>) roleRepo.findAll();
    }

    public void save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {

            User existingUser = userRepo.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);

        }
        userRepo.save(user);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email) {

        //Get user by email from db
        User userByEmail = userRepo.getUserByEmail(email);

        //Check if user doesnt exists
        if (userByEmail == null) {
            return true;
        }


        boolean isCreatingNewUser = (id == null);

        if (isCreatingNewUser) {
            if (userByEmail != null) {
                return false;
            }
        } else {
            if (!userByEmail.getId().equals(id)) {
                return false;
            }
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundException {

        try {
            return userRepo.findById(id).get();

        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Could not find any user with id " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long count = userRepo.countById(id);

        if(count == null || count == 0){
            throw new UserNotFoundException("Could not find any user with id " + id);

        }

        userRepo.deleteById(id);

    }
}
