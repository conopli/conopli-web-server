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
    String num;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String singer;

    @Column(nullable = false)
    String lyricist;

    @Column(nullable = false)
    String composer;

    @Column(nullable = false)
    String youtubeUrl;

    @Column(nullable = false)
    String nation;

}