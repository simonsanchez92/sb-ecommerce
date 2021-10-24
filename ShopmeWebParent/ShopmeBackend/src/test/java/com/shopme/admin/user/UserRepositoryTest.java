package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser() {

        Role roleAdmin = entityManager.find(Role.class, 1);

        User userSimon = new User("simonsanchez.1992@gmail.com", "123456", "Simon", "Sanchez");
        userSimon.addRole(roleAdmin);

        User savedUser = repo.save(userSimon);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRoles() {

        User userCamilo = new User("camilosanchez@gmail.com", "123456", "Camilo", "Sanchez");

        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userCamilo.addRole(roleEditor);
        userCamilo.addRole(roleAssistant);

        User savedUser = repo.save(userCamilo);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetAllUsers() {
        Iterable<User> users = repo.findAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById() {
        Optional<User> user = repo.findById(1);
        System.out.println(user);
        assertThat(user.isPresent()).isTrue();

    }

    @Test
    public void testUpdateUser() {
        User user = repo.findById(1).get();

        user.setEnabled(true);
        user.setEmail("simonsan92@gmail.com");

        repo.save(user);
    }

    @Test
    public void testUpdateRoles() {
        User user = repo.findById(2).get();

        user.getRoles().remove(new Role(3));

        user.getRoles().add(new Role(2));

        repo.save(user);
    }

    @Test
    public void testDeleteUser() {
        int userId = 2;

        repo.deleteById(userId);

        assertThat(repo.findById(userId)).isNotPresent();

    }

    @Test
    public void testGetUserByEmail() {
        String email = "simonsan92@gmail.com";
        User user = repo.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 1;
       Long count = repo.countById(id);

       assertThat(count).isNotNull().isGreaterThan(0);
    }
}
