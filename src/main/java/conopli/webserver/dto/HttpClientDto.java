package conopli.webserver.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HttpClientDto {

    Object data;


    HttpClientDto(Object data) {
        this.data = data;
    }

    public static HttpClientDto of(Object data) {
        return new HttpClientDto(data);
    }
}
