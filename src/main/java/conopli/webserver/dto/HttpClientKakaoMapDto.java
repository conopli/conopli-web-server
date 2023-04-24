package conopli.webserver.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HttpClientKakaoMapDto {
    Object documents;

    Object meta;

    HttpClientKakaoMapDto(Object documents, Object meta) {
        this.documents = documents;
        this.meta = meta;
    }

    public static HttpClientPageDto of(Object documents, Object meta) {
        return new HttpClientPageDto(documents, meta);
    }
}
