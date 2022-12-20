package com.example.library.service;

import com.example.library.exception.AuthorNotFoundException;
import com.example.library.model.UserDetailsImpl;
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
        return new UserDetailsImpl(authorRepository.findByEmail(username).orElseThrow(() -> new AuthorNotFoundException(username)));
    }
}
