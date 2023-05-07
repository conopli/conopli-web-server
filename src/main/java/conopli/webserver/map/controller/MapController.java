package conopli.webserver.map.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import conopli.webserver.dto.PageResponseDto;
import conopli.webserver.map.dto.MapSearchDto;
import conopli.webserver.map.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private String mapUrl;

    private final MapService mapService;

    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping
    public ModelAndView getMap(
            @ModelAttribute MapSearchDto dto
            ) throws JsonProcessingException {
        PageResponseDto res = mapService.searchKakaoMap(dto);
        String response = mapper.writeValueAsString(res);
        return new ModelAndView(
                "map",
                Map.of("mapUrl", mapUrl,
                        "response", response,
                        "lat",dto.getLat(),
                        "lng",dto.getLng()
                ));
    }
}
