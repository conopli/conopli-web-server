package conopli.webserver.music.dto;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserMusicRequestDto {

    private Long userId;

    private Long playListId;

    private List<String> musicNum;

}
