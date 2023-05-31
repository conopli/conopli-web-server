package conopli.webserver.music.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserMusicRequestDto {

    Long userId;

    Long playListId;

    String musicNum;

}
