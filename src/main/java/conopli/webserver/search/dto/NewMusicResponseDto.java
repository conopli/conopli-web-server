package conopli.webserver.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NewMusicResponseDto {

    private String num;

    private String title;

    private String singer;

    private String lyricist;

    private String composer;

    private String youtubeUrl;

    private String nation;

}
