package conopli.webserver.user.entity;

import conopli.webserver.audit.Auditable;
import conopli.webserver.constant.LoginType;
import conopli.webserver.constant.UserStatus;
import conopli.webserver.playlist.entity.PlayList;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserStatus userStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @ToString.Exclude
    @OrderBy("playListId")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Setter
    private Set<PlayList> playList = new LinkedHashSet<>();

    public void addPlayList(PlayList playList) {
        this.playList.add(playList);
        playList.addUser(this);
    }

}
