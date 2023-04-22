package conopli.webserver.user.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {

    private Long userId;

    private String email;

    private String nickName;

    private String userStatus;

}
