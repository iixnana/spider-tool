package com.spider.routes.service;

import com.spider.routes.dto.SignUpDto;
import com.spider.routes.dto.UserDto;
import com.spider.routes.model.User;
import com.spider.routes.repository.RoleRepository;
import com.spider.routes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(SignUpDto user) {
        User newUser = new User();
        newUser.setUsername(user.getLogin());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(CharBuffer.wrap(user.getPassword())));

        /* TODO: Add roles
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User role not found."));
        newUser.getRoles().add(userRole);
        */

        return userRepository.save(newUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto signUp(SignUpDto user) {
        User newUser = createUser(user);
        return new UserDto(newUser.getId(), newUser.getFirstName(), newUser.getLastName(), newUser.getUsername(), "token");
    }

    public void signOut(UserDto user) {
        // nothing to do at the moment
    }
}
