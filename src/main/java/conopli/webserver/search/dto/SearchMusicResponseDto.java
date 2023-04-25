package conopli.webserver.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchMusicResponseDto {
    String num;
    String title;
    String singer;
    String lyricist;
    String composer;
    String youtubeUrl;
    String nation;
}
