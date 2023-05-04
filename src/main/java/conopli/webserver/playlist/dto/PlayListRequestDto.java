package conopli.webserver.playlist.dto;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlayListRequestDto {

    private Long userId;

    private String title;

    private String color;

    private String emoji;

}
