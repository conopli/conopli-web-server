package conopli.webserver.music.controller;


import conopli.webserver.dto.PageResponseDto;
import conopli.webserver.dto.ResponseDto;
import conopli.webserver.music.dto.UserMusicDto;
import conopli.webserver.music.dto.UserMusicRequestDto;
import conopli.webserver.music.service.UserMusicService;
import conopli.webserver.playlist.dto.PlayListModifyRequestDto;
import conopli.webserver.playlist.dto.PlayListRequestDto;
import conopli.webserver.utils.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-music")
@Slf4j
@RequiredArgsConstructor
public class UserMusicController {

    private final UserMusicService userMusicService;

    @GetMapping("/playlist/{userId}")
    public ResponseEntity<?> userPlaylist(
            @PathVariable Long userId
    ) {
        // Todo : User의 플레이 리스트 조회
        ResponseDto userPlayList = userMusicService.findUserPlayList(userId);
        return ResponseEntity.ok(userPlayList);
    }

    @GetMapping("/{playListId}")
    public ResponseEntity<?> userMusic(
            @PathVariable Long playListId,
            @PageableDefault(page = 0, size = 20, sort = "orderNum", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        // Todo : User의 플레이 리스트의 음악 조회
        PageResponseDto userMusic = userMusicService.findUserMusic(playListId, pageable);
        return ResponseEntity.ok(userMusic);
    }

    @PostMapping("/playlist")
    public ResponseEntity<?> savePlaylist(
            @RequestBody PlayListRequestDto requestDto
    ) {
        // Todo : User의 플레이 리스트 생성
        ResponseDto response = userMusicService.createUserPlayList(requestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> saveUserMusic(
            @RequestBody UserMusicRequestDto requestDto
    ) {
        // Todo : User의 플레이 리스트에 음악 저장
        ResponseDto response = userMusicService.createUserMusic(requestDto);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/playlist/{playListId}")
    public ResponseEntity<?> modifyPlayList(
            @PathVariable Long playListId,
            @RequestBody PlayListRequestDto requestDto
    ) {
        // Todo : User의 특정 플레이리스트 수정
        ResponseDto response = userMusicService.modifyPlayList(playListId, requestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/contents")
    public ResponseEntity<?> modifyUserMusic(
            @RequestBody PlayListModifyRequestDto requestDto
            ) {
        // Todo : User 플레이 리스트의 음악 수정
        PageResponseDto response = userMusicService.modifyUserMusic(requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/playlist/{playListId}")
    public ResponseEntity<?> deletePlayList(
            @PathVariable Long playListId
    ) {
        // Todo : User 특정 플레이 리스트 삭제
        userMusicService.deleteUserPlayList(playListId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<?> deleteUserMusic(
            @RequestBody PlayListModifyRequestDto requestDto
    ) {
        // Todo : User 플레이 리스트의 음악 삭제
        userMusicService.deleteUserMusic(requestDto);
        return ResponseEntity.noContent().build();
    }
}
