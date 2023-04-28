package conopli.webserver.playlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlayListRequestDto {

    private String title;

    private String color;

    private String emoji;

}
