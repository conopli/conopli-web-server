package conopli.webserver.user.service;

import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.constant.LoginType;
import conopli.webserver.constant.UserStatus;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User verifiedUserById(Long userId) {

        return null;
    }

    public User verifiedUserByEmail(String email) {
        return null;
    }

    public User createOrVerifiedUserByEmailAndLoginType(String email,String loginType) {
        try {
            User findUser = userRepository.findUserByEmail(email);
            if (findUser.getLoginType().equals(LoginType.valueOf(loginType.toUpperCase()))) {
                return findUser;
            } else {
                throw new ServiceLogicException(ErrorCode.EXIST_USER);
            }
        } catch (ServiceLogicException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND_USER)) {
                User user = User.builder()
                        .userStatus(UserStatus.VERIFIED)
                        .email(email)
                        .loginType(LoginType.valueOf(loginType.toUpperCase()))
                        .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                        .build();
                return userRepository.saveUser(user);
            } else {
                throw e;
            }
        }
    }
}
