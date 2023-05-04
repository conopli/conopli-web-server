package conopli.webserver.playlist.entity;


import conopli.webserver.audit.Auditable;
import conopli.webserver.music.entity.UserMusic;
import conopli.webserver.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlayList extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playListId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String emoji;

    @Column(nullable = false)
    @Setter
    private int countingOrder;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @OrderBy("userMusicId")
    @OneToMany(mappedBy = "playList", cascade = CascadeType.ALL)
    @Setter
    private Set<UserMusic> userMusic = new LinkedHashSet<>();

    public void addUser(User user) {
        this.user = user;
    }

    public void addUserMusic(UserMusic userMusic) {
        this.userMusic.add(userMusic);
        userMusic.addPlayList(this);
    }

}
