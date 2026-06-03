package edu.barnett.gearwood_puzzle_store.services;

import edu.barnett.gearwood_puzzle_store.dtos.JwtResponse;
import edu.barnett.gearwood_puzzle_store.dtos.LoginRequestDto;
import edu.barnett.gearwood_puzzle_store.entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthService {

    JwtResponse authenticateAndGenerateToken(LoginRequestDto user);

    void clearJwtCookie(HttpServletResponse response);

    Cookie loginAndCreateJwtCookie(LoginRequestDto user) throws BadCredentialsException;

    User getCurrentUser();
}
