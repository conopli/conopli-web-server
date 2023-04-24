package conopli.webserver.map.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
public class MapSearchDto {
    String searchType;
    // 경도 x
    String lng;
    // 위도 y
    String lat;
}
