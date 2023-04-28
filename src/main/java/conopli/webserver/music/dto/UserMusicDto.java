package conopli.webserver.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserMusicDto {

    private String userMusicId;

    private String playListId;

    private String musicId;

    private String num;

    private String title;

    private String singer;

    private String lyricist;

    private String composer;

    private String youtubeUrl;

    private String nation;

    private Integer orderNum;

}
