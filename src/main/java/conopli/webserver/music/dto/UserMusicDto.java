package conopli.webserver.music.dto;

import conopli.webserver.music.entity.UserMusic;
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

    private int orderNum;

    private String kyNum;

    private boolean mrSound;

    private UserMusicDto (UserMusic music) {
        this.userMusicId = String.valueOf(music.getUserMusicId());
        this.musicId = String.valueOf(music.getMusicId());
        this.playListId = String.valueOf(music.getPlayList().getPlayListId());
        this.num = music.getNum();
        this.title = music.getTitle();
        this.singer = music.getSinger();
        this.lyricist = music.getLyricist();
        this.composer = music.getComposer();
        this.youtubeUrl = music.getYoutubeUrl();
        this.nation = music.getNation();
        this.orderNum = music.getOrderNum();
        this.kyNum = music.getKyNum();
        this.mrSound = music.isMrSound();
    }

    public static UserMusicDto of(UserMusic music) {
        return new UserMusicDto(music);
    }
}
