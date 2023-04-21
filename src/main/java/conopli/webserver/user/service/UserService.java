package conopli.webserver.user.service;

import conopli.webserver.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    public User verifiedUserById(Long userId) {
        return null;
    }
}
