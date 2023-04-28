package conopli.webserver.search.controller;


import conopli.webserver.dto.PageResponseDto;
import conopli.webserver.dto.ResponseDto;
import conopli.webserver.search.dto.*;
import conopli.webserver.utils.StubUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    @GetMapping
    public ResponseEntity<?> searchMusic(
            @ModelAttribute SearchDto requestDto,
            @PageableDefault(page = 0, size = 10, sort = "num", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        // Todo : 음악 검색
        List<SearchMusicResponseDto> dtoList = List.of(StubUtils.createSearchMusicDto(), StubUtils.createSearchMusicDto());
        Page page = new PageImpl(dtoList);
        PageResponseDto response = PageResponseDto.of(dtoList, page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<?> popularMusic(
            @ModelAttribute PopularRequestDto requestDto
    ) {
        // Todo : 인기곡 검색
        List<PopularResponseDto> dtoList = List.of(StubUtils.createPopularResponseDto(), StubUtils.createPopularResponseDto());
        return ResponseEntity.ok(ResponseDto.of(dtoList));
    }

    @GetMapping("/new-music")
    public ResponseEntity<?> newMusic() {
        // Todo : 신곡 검색
        List<NewMusicResponseDto> dtoList = List.of(StubUtils.createNewMusicDto(), StubUtils.createNewMusicDto());
        return ResponseEntity.ok(ResponseDto.of(dtoList));
    }

}
