package conopli.webserver.home;

import conopli.webserver.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    @GetMapping("/redirect")
    public ModelAndView getRedirect() {
        return new ModelAndView("loading");
    }

    @GetMapping("/policy")
    public ModelAndView getPolicy() {
        return new ModelAndView("policy");
    }

    @GetMapping("/version")
    @ResponseBody
    public ResponseEntity<?> getVersion() {
        Map<String, String> response = Map.of("version", "0.2");
        return ResponseEntity.ok(ResponseDto.of(response));
    }
}
