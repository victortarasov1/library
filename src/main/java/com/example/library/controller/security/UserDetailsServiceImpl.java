package com.example.library.controller.security;

import com.example.library.exception.AuthorNotFoundException;
import com.example.library.model.AuthorUserDetails;
import com.example.library.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthorRepository authorRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthorUserDetails(authorRepository.findByEmail(username).orElseThrow(() -> new AuthorNotFoundException(username)));
    }
}
