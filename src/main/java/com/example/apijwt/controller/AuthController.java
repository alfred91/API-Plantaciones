package com.example.apijwt.controller;

import com.example.apijwt.dto.LoginRequest;
import com.example.apijwt.dto.LoginResponse;
import com.example.apijwt.dto.UserRegisterDTO;
import com.example.apijwt.entity.UserEntity;
import com.example.apijwt.security.JwtTokenProvider;
import com.example.apijwt.security.UserDetailsServiceImpl;
import com.example.apijwt.service.UserEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;

@RestController
public class AuthController {

    @Autowired
    private UserEntityService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authManager;


    @PostMapping("/auth/register")
    public UserEntity save(@RequestBody UserRegisterDTO userDTO){
        return this.userService.save(userDTO);
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginDTO){

        Authentication authDTO = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());

        //Este método es el que llama al AuthenticationManager correspondiente para ver si la autenticación es correcta
        Authentication authentication = this.authManager.authenticate(authDTO);

        //El método nos devuelve un UserEntity (con UserDetailService) para con esos datos generar el token
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String token = this.jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                token);
    }
}