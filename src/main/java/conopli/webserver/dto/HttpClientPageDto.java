package conopli.webserver.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HttpClientPageDto {

    Object data;

    Object pageInfo;

    HttpClientPageDto(Object data, Object pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }

    public static HttpClientPageDto of(Object data, Object pageInfo) {
        return new HttpClientPageDto(data, pageInfo);
    }
}