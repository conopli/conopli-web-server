package conopli.webserver.auth.token.refresh.repository;

import conopli.webserver.auth.token.refresh.entity.RefreshEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshRepository extends CrudRepository<RefreshEntity, String> {
}
