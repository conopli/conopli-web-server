package conopli.webserver.music.entity;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import conopli.webserver.audit.Auditable;
import conopli.webserver.dto.HttpClientDto;
import conopli.webserver.playlist.entity.PlayList;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class UserMusic extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userMusicId;

    @Column(nullable = false)
    private Long musicId;

    @Column(nullable = false)
    private String num;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String singer;

    @Column(nullable = false)
    private String lyricist;

    @Column(nullable = false)
    private String composer;

    @Column(nullable = false)
    private String youtubeUrl;

    @Column(nullable = false)
    private String nation;

    @Column(nullable = false)
    @Setter
    private int orderNum;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private PlayList playList;

    public void addPlayList(PlayList playList) {
        this.playList = playList;
    }

    private UserMusic(HttpClientDto dto) {
        Gson gson = new Gson();
        JsonElement el = JsonParser.parseString(gson.toJson(dto.getData()));
        this.musicId = Long.valueOf(el.getAsJsonObject().get("musicId").getAsString());
        this.num = el.getAsJsonObject().get("num").getAsString();
        this.title = el.getAsJsonObject().get("title").getAsString();
        this.singer = el.getAsJsonObject().get("singer").getAsString();
        this.lyricist = el.getAsJsonObject().get("lyricist").getAsString();
        this.composer = el.getAsJsonObject().get("composer").getAsString();
        this.youtubeUrl = el.getAsJsonObject().get("youtubeUrl").getAsString();
        this.nation = el.getAsJsonObject().get("nation").getAsString();
    }

    public static UserMusic of(HttpClientDto dto) {
        return new UserMusic(dto);
    }

}
