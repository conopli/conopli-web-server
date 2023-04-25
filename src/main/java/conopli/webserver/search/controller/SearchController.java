package conopli.webserver.search.controller;


import conopli.webserver.search.dto.PopularDto;
import conopli.webserver.search.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<?> popularMusic(
            @ModelAttribute PopularDto requestDto
    ) {
        // Todo : 인기곡 검색

        return ResponseEntity.ok().build();
    }

    @GetMapping("/new-music")
    public ResponseEntity<?> newMusic() {
        // Todo : 신곡 검색

        return ResponseEntity.ok().build();
    }
}
