package com.api.ecommerce.users.infrastructure.security;

import com.api.ecommerce.shared.security.jwt.JwtService;
import com.api.ecommerce.users.domain.AppUser;
import com.api.ecommerce.users.infrastructure.persistence.IAppUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private IAppUserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserDetailsServiceImp(IAppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username).orElseThrow();
        return new CustomUserDetails(user);
    }
    public Authentication authenticate(String username, String password){
        CustomUserDetails user = (CustomUserDetails) this.loadUserByUsername(username);
        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("Invalid username or password!");
        }
        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
    }

}
