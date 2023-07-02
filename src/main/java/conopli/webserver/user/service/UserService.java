package conopli.webserver.user.service;

import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.constant.LoginType;
import conopli.webserver.constant.UserStatus;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.playlist.entity.PlayList;
import conopli.webserver.user.dto.UserDto;
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

    public UserDto searchUser(Long userId) {
        return UserDto.of(userRepository.findUserById(userId));
    }

    public void deleteUser(Long userId) {

    }

    public User verifiedUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    public User verifiedUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public UserDto createOrVerifiedUserByEmailAndLoginType(String email, String loginType) {
        try {
            User findUser = userRepository.findUserByEmail(email);
            if (findUser.getLoginType().equals(LoginType.valueOf(loginType.toUpperCase()))) {
                return UserDto.of(findUser);
            } else {
                throw new ServiceLogicException(ErrorCode.EXIST_USER);
            }
        } catch (ServiceLogicException e) {
            if (e.getErrorCode().equals(ErrorCode.NOT_FOUND_USER)) {
                PlayList playList = PlayList.builder()
                        .emoji("128561")
                        .color("18")
                        .title("기본 플레이 리스트")
                        .build();
                User user = User.builder()
                        .userStatus(UserStatus.VERIFIED)
                        .email(email)
                        .loginType(LoginType.valueOf(loginType.toUpperCase()))
                        .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                        .build();
                user.addPlayList(playList);
                return UserDto.of(userRepository.saveUser(user));
            } else {
                throw e;
            }
        }
    }
}
