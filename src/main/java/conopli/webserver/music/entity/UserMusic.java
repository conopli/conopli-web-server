package conopli.webserver.music.entity;

import conopli.webserver.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class UserMusic extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userMusicId;

    @Column(nullable = false, unique = true)
    private Long musicId;

    @Column(nullable = false, unique = true)
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
    private String orderNum;

}
