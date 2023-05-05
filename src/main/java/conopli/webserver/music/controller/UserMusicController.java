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
        return ResponseEntity.ok(ResponseDto.of(StubUtils.createPlayListDto()));
    }

    @PostMapping
    public ResponseEntity<?> saveUserMusic(
            @RequestBody UserMusicRequestDto requestDto
    ) {
        // Todo : User의 플레이 리스트에 음악 저장
        return ResponseEntity.ok(ResponseDto.of(StubUtils.createUserMusicDto(1)));
    }


    @PatchMapping("/playlist/{playListId}")
    public ResponseEntity<?> modifyPlayList(
            @PathVariable Long playListId,
            @RequestBody PlayListRequestDto requestDto
    ) {

        // Todo : User의 특정 플레이리스트 수정
        return ResponseEntity.ok(ResponseDto.of(StubUtils.createPlayListDto()));
    }

    @PatchMapping
    public ResponseEntity<?> modifyUserMusic(
            @RequestBody PlayListModifyRequestDto requestDto
            ) {
        // Todo : User 플레이 리스트의 음악 수정
        UserMusicDto dto1 = StubUtils.createUserMusicDto(1);
        UserMusicDto dto2 = StubUtils.createUserMusicDto(2);
        List<UserMusicDto> dtoList = List.of(dto1, dto2);
        Page page = new PageImpl(dtoList);
        PageResponseDto response = PageResponseDto.of(dtoList, page);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/playlist/{playListId}")
    public ResponseEntity<?> deletePlayList(
            @PathVariable Long playListId
    ) {
        // Todo : User 특정 플레이 리스트 삭제
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userMusicId}")
    public ResponseEntity<?> deleteUserMusic(
            @PathVariable Long userMusicId
    ) {
        // Todo : User 플레이 리스트의 음악 삭제
        return ResponseEntity.noContent().build();
    }



}
