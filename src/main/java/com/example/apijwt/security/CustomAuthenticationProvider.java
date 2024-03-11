package com.example.apijwt.security;

import com.example.apijwt.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //Comprobar la password
        UserEntity user = (UserEntity) userService.loadUserByUsername(authentication.getName());

        //Comprobamos que la contraseña en Json si le aplicamos el encoder coincide (matches) con la
        //contraseña encriptada almacenada en BBDD
        if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user,
                    authentication.getCredentials(), authentication.getAuthorities());
        } else {
            throw new AuthenticationException("Invalid credentials") {};
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
