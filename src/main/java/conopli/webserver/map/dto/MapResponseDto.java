package conopli.webserver.map.dto;

import conopli.webserver.dto.HttpClientKakaoMapDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class MapResponseDto {
    String addressName;
    String roadAddressName;
    String placeName;
    String lng;
    String lat;

    MapResponseDto(Map<String, String> dto) {
        this.addressName = dto.get("address_name");
        this.roadAddressName = dto.get("road_address_name");
        this.placeName = dto.get("place_name");
        this.lng = dto.get("x");
        this.lat = dto.get("y");
    }

    public static MapResponseDto of(Map<String, String> dto) {
        return new MapResponseDto(dto);
    }



}
