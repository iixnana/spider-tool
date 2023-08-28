package com.spider.routes.service;

import com.spider.routes.dto.CredentialsDto;
import com.spider.routes.dto.UserDto;
import com.spider.routes.model.User;
import com.spider.routes.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;


@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserDto authenticate(CredentialsDto credentialsDto) {
        User user = userRepository.findByUsername(credentialsDto.getLogin());
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username:: " + credentialsDto.getLogin());
        }
        // TODO: remove master password from plain text
        String encodedMasterPassword = passwordEncoder.encode(CharBuffer.wrap("master"));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword()) ||
                passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), encodedMasterPassword)) {
            return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), "token");
        }
        throw new RuntimeException("Invalid password");
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByUsername(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username:: " + login);
        }
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), "token");
    }
}
