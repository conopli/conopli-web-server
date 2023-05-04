package conopli.webserver.playlist.repository;

import conopli.webserver.playlist.entity.PlayList;
import conopli.webserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayListJpaRepository extends JpaRepository<PlayList, Long> {

    List<PlayList> findAllByUser(User user);
}
