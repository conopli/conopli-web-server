package conopli.webserver.music.repository;

import conopli.webserver.music.entity.UserMusic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMusicJpaRepository extends JpaRepository<UserMusic, Long> {
}
