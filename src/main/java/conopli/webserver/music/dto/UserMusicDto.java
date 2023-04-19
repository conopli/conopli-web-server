package conopli.webserver.music.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserMusicDto {

    Long userMusicId;

    Long musicId;

    String num;

    String title;

    String singer;

    String lyricist;

    String composer;

    String youtubeUrl;

    String nation;
}
