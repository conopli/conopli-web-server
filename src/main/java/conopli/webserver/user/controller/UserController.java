package conopli.webserver.user.controller;


import conopli.webserver.search.dto.SearchDto;
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
    public ResponseEntity<?> searchUser() {
        // Todo : 회원 정보 검색

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser() {
        // Todo : 회원 탈퇴

        return ResponseEntity.ok().build();
    }
}
