package conopli.webserver.user.controller;


import conopli.webserver.dto.ResponseDto;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.user.service.UserService;
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

    @GetMapping("/{userId}")
    public ResponseEntity<?> searchUser(
            @PathVariable Long userId
    ) {
        // Todo : 회원 정보 검색
        UserDto response = userService.searchUser(userId);
        return ResponseEntity.ok(ResponseDto.of(response));
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
