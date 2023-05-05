package conopli.webserver.playlist.dto;


import conopli.webserver.playlist.entity.PlayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlayListDto {

    private Long playListId;

    private String title;

    private String color;

    private String emoji;

    public static PlayListDto of(PlayList list) {
        return PlayListDto.builder()
                .playListId(list.getPlayListId())
                .title(list.getTitle())
                .color(list.getColor())
                .emoji(list.getEmoji())
                .build();
    }

}
