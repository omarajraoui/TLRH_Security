package com.tlrhv3.sec.Controller;

import com.tlrhv3.sec.Dto.LoginDto;
import com.tlrhv3.sec.Dto.RegisterDto;
import com.tlrhv3.sec.Dto.TokenDto;
import com.tlrhv3.sec.Entity.RefreshToken;
import com.tlrhv3.sec.Entity.User;
import com.tlrhv3.sec.Jwt.JwtHelper;
import com.tlrhv3.sec.Repository.RefreshTokenRepository;
import com.tlrhv3.sec.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    //autowired error!

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/login")
    @Transactional //any db op is reverted if an exception is thrown
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenSring = jwtHelper.generateRefreshToken(user, refreshToken.getId());
        return ResponseEntity.ok(new TokenDto(user.getId(), accessToken, refreshTokenSring));
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepository.save(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        String accessToken = jwtHelper.generateAccessToken(user);
        String refreshTokenSring = jwtHelper.generateRefreshToken(user, refreshToken.getId());
        return ResponseEntity.ok(new TokenDto(user.getId(), accessToken, refreshTokenSring));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto) {
//        tokenDto to invalidate the refreshtoken
        String refreshTokenString = tokenDto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            //valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }
        throw new BadCredentialsException("invalid token");


    }
}












