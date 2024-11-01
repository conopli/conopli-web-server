package conopli.webserver.user.dto;


import conopli.webserver.user.entity.User;
import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {

    private Long userId;

    private String email;

    private String userStatus;

    private String loginType;

    public static UserDto of(User user){
        return UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .loginType(user.getLoginType().name())
                .userStatus(user.getUserStatus().name())
                .build();
    }

}
