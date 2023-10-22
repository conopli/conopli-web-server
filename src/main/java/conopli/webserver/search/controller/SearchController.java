package conopli.webserver.search.controller;


import conopli.webserver.dto.HttpClientDto;
import conopli.webserver.dto.HttpClientPageDto;
import conopli.webserver.dto.PageResponseDto;
import conopli.webserver.dto.ResponseDto;
import conopli.webserver.search.dto.*;
import conopli.webserver.service.HttpClientService;
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
@RequestMapping("/api/search")
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    private final HttpClientService httpClientService;

    @GetMapping
    public ResponseEntity<?> searchMusic(
            @ModelAttribute SearchDto requestDto
    ) {
        // Todo : 음악 검색
        HttpClientPageDto response = httpClientService.generateSearchMusicRequest(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<?> popularMusic(
            @ModelAttribute PopularRequestDto requestDto
    ) {
        // Todo : 인기곡 검색
        HttpClientDto response = httpClientService.generatePopularMusicRequest(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/new-music")
    public ResponseEntity<?> newMusic(
            @RequestParam String yy,
            @RequestParam String mm
    ) {
        // Todo : 신곡 검색
        HttpClientDto response = httpClientService.generateNewMusicRequest(yy, mm);
        return ResponseEntity.ok(response);
    }

}
