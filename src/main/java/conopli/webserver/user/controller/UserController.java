package conopli.webserver.user.controller;


import conopli.webserver.dto.ResponseDto;
import conopli.webserver.search.dto.SearchDto;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.utils.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/{userId}")
    public ResponseEntity<?> searchUser(
            @PathVariable String userId
    ) {
        // Todo : 회원 정보 검색
        UserDto userDto = StubUtils.createUserDto();
        return ResponseEntity.ok(ResponseDto.of(userDto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable String userId
    ) {
        // Todo : 회원 탈퇴
        return ResponseEntity.noContent().build();
    }
}
