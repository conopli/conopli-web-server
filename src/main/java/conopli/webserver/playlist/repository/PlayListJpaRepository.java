package conopli.webserver.playlist.repository;

import conopli.webserver.playlist.entity.PlayList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayListJpaRepository extends JpaRepository<PlayList, Long> {
}
