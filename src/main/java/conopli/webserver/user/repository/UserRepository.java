package conopli.webserver.user.repository;

import conopli.webserver.constant.LoginType;
import conopli.webserver.user.entity.User;

public interface UserRepository {

    User saveUser(User user);

    User findUserByEmail(String email);
}
