package conopli.webserver.map.service;

import conopli.webserver.dto.HttpClientKakaoMapDto;
import conopli.webserver.dto.PageResponseDto;
import conopli.webserver.map.dto.MapResponseDto;
import conopli.webserver.map.dto.MapSearchDto;
import conopli.webserver.service.HttpClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final HttpClientService httpClientService;

    public PageResponseDto searchKakaoMap(
            MapSearchDto dto
    ) {
        HttpClientKakaoMapDto mapList = httpClientService.generateKakaoMapRequest(dto);
        List<Map<String, String>> documents = (List<Map<String, String>>) mapList.getDocuments();
        List<MapResponseDto> list = documents.stream()
                .map(MapResponseDto::of)
                .collect(Collectors.toList());
        Page page = new PageImpl(list);
        return PageResponseDto.of(list, page);
    }
}
