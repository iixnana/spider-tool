package com.spider.routes.service;

import com.spider.routes.dto.SignUpDto;
import com.spider.routes.dto.UserDto;
import com.spider.routes.model.Role;
import com.spider.routes.model.RoleName;
import com.spider.routes.model.User;
import com.spider.routes.repository.RoleRepository;
import com.spider.routes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User role not found."));
        newUser.getRoles().add(userRole);

        return userRepository.save(newUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto signUp(SignUpDto user) {
        return new UserDto(1L, "Kamile", "Nanartonyte", "login", "token");
    }

    public void signOut(UserDto user) {
        // nothing to do at the moment
    }
}
