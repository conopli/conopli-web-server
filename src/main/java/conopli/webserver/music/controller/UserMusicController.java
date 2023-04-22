package conopli.webserver.music.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-music")
@Slf4j
@RequiredArgsConstructor
public class UserMusicController {

    @GetMapping
    public ResponseEntity<?> userMusic() {
        return ResponseEntity.ok().build();
    }

}
