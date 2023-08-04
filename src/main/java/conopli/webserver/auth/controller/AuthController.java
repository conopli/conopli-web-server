package conopli.webserver.auth.controller;

import conopli.webserver.auth.dto.AuthSuccessTokenResponseDto;
import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.auth.service.AuthService;
import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.service.HttpClientService;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Validated
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginDto loginDto,
            HttpServletResponse response
    ) {
        authService.login(loginDto,response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify-user")
    public ResponseEntity<?> verifyUser(
            HttpServletRequest request
    ) throws IOException {
        authService.verifyUser(request.getHeader("Authorization"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reissue-token/{userId}")
    public ResponseEntity<AuthSuccessTokenResponseDto> reIssueToken(
            HttpServletResponse response,
            @PathVariable @Positive Long userId
    ) throws IOException {
        authService.reIssueToken(response, userId);
        return new ResponseEntity<>(
                AuthSuccessTokenResponseDto.of(response),
                HttpStatus.OK
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request,response);
        return ResponseEntity.ok().build();
    }


}
