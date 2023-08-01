package conopli.webserver.user.controller;


import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.auth.service.AuthService;
import conopli.webserver.dto.ResponseDto;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthService authService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> searchUser(
            @PathVariable Long userId
    ) {
        // Todo : 회원 정보 검색
        UserDto response = userService.searchUser(userId);
        return ResponseEntity.ok(ResponseDto.of(response));
    }

    @PatchMapping
    public ResponseEntity<?> reActivationUser(
            @RequestBody LoginDto loginDto,
            HttpServletResponse response
    ) {
        UserDto userDto = authService.reActivationUser(loginDto, response);
        return ResponseEntity.ok(ResponseDto.of(userDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long userId
    ) {
        // Todo : 회원 탈퇴
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
