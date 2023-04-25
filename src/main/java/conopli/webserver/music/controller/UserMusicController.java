package conopli.webserver.music.controller;


import conopli.webserver.music.dto.UserMusicRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-music")
@Slf4j
@RequiredArgsConstructor
public class UserMusicController {

    @GetMapping("/playlist/{userId}")
    public ResponseEntity<?> userPlaylist(
            @PathVariable String userId
    ) {
        // Todo : User의 플레이 리스트 조회

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{playListId}")
    public ResponseEntity<?> userMusic(
            @PathVariable String playListId
    ) {
        // Todo : User의 플레이 리스트의 음악 조회

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> saveUserMusic(
            @RequestBody UserMusicRequestDto requestDto
    ) {
        // Todo : User의 플레이 리스트에 음악 저장
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/playlist")
    public ResponseEntity<?> modifyPlayList(

    ) {
        // Todo : User의 특정 플레이리스트 수정
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> modifyUserMusic(

    ) {
        // Todo : User 플레이 리스트의 음악 수정
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/playlist/{playListId}")
    public ResponseEntity<?> deletePlayList(
            @PathVariable String playListId
    ) {
        // Todo : User 특정 플레이 리스트 삭제
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userMusicId}")
    public ResponseEntity<?> deleteUserMusic(
            @PathVariable String userMusicId
    ) {
        // Todo : User 플레이 리스트의 음악 삭제
        return ResponseEntity.ok().build();
    }


}
