package conopli.webserver.music.repository;

import conopli.webserver.music.entity.UserMusic;
import conopli.webserver.playlist.entity.PlayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMusicJpaRepository extends JpaRepository<UserMusic, Long> {

    Optional<UserMusic> findUserMusicByNum(String num);

    Page<UserMusic> findUserMusicByPlayList(PlayList playList, Pageable pageable);
}
