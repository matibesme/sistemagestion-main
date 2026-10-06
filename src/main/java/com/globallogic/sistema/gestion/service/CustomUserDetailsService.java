package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.model.User;
import com.globallogic.sistema.gestion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads a user by their username.
     * This method is used by Spring Security to perform authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name())).toList()
        );
    }
}
