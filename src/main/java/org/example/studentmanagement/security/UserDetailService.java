package org.example.studentmanagement.security;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.repository.UserRepository;
import org.example.studentmanagement.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byEmail = userService.findByEmail(username);

        if (byEmail.isEmpty()) {
            throw new UsernameNotFoundException("User with email" + username + " does not exists!");
        }

        return new SpringUser(byEmail.get());
    }
}
