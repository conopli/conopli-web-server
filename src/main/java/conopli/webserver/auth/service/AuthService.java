package conopli.webserver.auth.service;

import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.service.HttpClientService;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtTokenizer jwtTokenizer;

    private final UserService userService;

    private final HttpClientService httpClientService;

    public void login(
            LoginDto loginDto,
            HttpServletResponse response
    ) {
        String email = httpClientService.generateLoginRequest(loginDto);
        UserDto user = userService.createOrVerifiedUserByEmailAndLoginType(email, loginDto.getLoginType());
        jwtTokenizer.delegateToken(user.getEmail(), response);
        response.setHeader("userId", String.valueOf(user.getUserId()));
        response.setHeader("userStatus", user.getUserStatus());
    }

    public void verifyUser(
            String requestAccessToken
    ) {
        String accessToken = requestAccessToken.replace("Bearer ", "");
        jwtTokenizer.verifyAccessToken(accessToken);
        log.info("# Verify Login User");
    }

    public void reIssueToken(
            HttpServletResponse response,
            Long userId
    ) throws IOException {
        User user = userService.verifiedUserById(userId);
        jwtTokenizer.verifyRefreshToken(user.getEmail(), response);
        response.setHeader("userStatus", user.getUserStatus().name());
        response.setHeader("userId", user.getUserId().toString());
        log.info("# Reissue Token");
    }

    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        jwtTokenizer.deleteRefresh(request.getHeader("Authorization").replace("Bearer ", ""));
        log.info("# User Logout");
    }
}
