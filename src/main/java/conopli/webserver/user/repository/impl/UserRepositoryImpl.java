package conopli.webserver.user.repository.impl;

import conopli.webserver.constant.ErrorCode;
import conopli.webserver.constant.LoginType;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.repository.UserJpaRepository;
import conopli.webserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository repository;


    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public User findUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new ServiceLogicException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public void deleteUser(User user) {
        repository.delete(user);
    }
}
