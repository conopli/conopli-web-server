package conopli.webserver.home;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
