package conopli.webserver.user.repository;

import conopli.webserver.constant.LoginType;
import conopli.webserver.user.entity.User;

public interface UserRepository {

    User saveUser(User user);

    User findUserByEmail(String email);

    User findUserById(Long userId);

    void deleteUser(User user);

}
