package conopli.webserver.map.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("/maps")
@Slf4j
@RequiredArgsConstructor
public class MapController {

    @Value("${MAP_API_KEY}")
    private String mapApiKey;

    @GetMapping
    public ModelAndView getMap(

    ) {
        System.out.println(mapApiKey);
        return new ModelAndView(
                "map",
                Map.of("mapApiKey",mapApiKey
                ));
    }
}
