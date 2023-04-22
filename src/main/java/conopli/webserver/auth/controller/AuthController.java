package conopli.webserver.auth.controller;

import conopli.webserver.auth.dto.AuthSuccessTokenResponseDto;
import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.auth.token.Token;
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

    private final JwtTokenizer jwtTokenizer;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @ModelAttribute LoginDto loginDto,
            HttpServletResponse response
    ) {
        System.out.println(loginDto.getOauthAccessToken());
//        jwtTokenizer.delegateToken(new User(),response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify-user")
    public ResponseEntity<?> verifyUser(
            HttpServletRequest request
    ) throws IOException {
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.replace("Bearer ", "");
        jwtTokenizer.verifyAccessToken(accessToken);
        log.info("# Verify Login User");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reissue-token/{userId}")
    public ResponseEntity<AuthSuccessTokenResponseDto> reIssueToken(
            HttpServletResponse response,
            @PathVariable @Positive Long userId
    ) throws IOException {
        User user = userService.verifiedUserById(userId);
        jwtTokenizer.verifyRefreshToken(user.getEmail(), response);
        response.setHeader("userStatus", user.getUserStatus().name());
        response.setHeader("userId", user.getUserId().toString());
        log.info("# Reissue Token");
        return new ResponseEntity<>(AuthSuccessTokenResponseDto.of(response), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        jwtTokenizer.deleteRefresh(request.getHeader("Authorization"));
        log.info("# User Logout");
        return ResponseEntity.ok().build();
    }


}
