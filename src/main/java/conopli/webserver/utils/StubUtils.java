package conopli.webserver.utils;

import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.auth.token.JwtAuthorityUtils;
import conopli.webserver.constant.LoginType;
import conopli.webserver.constant.UserStatus;
import conopli.webserver.music.dto.UserMusicDto;
import conopli.webserver.music.dto.UserMusicRequestDto;
import conopli.webserver.playlist.dto.PlayListDto;
import conopli.webserver.playlist.dto.PlayListModifyRequestDto;
import conopli.webserver.playlist.dto.PlayListRequestDto;
import conopli.webserver.search.dto.*;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class StubUtils {

    public static PopularRequestDto createPopularRequestDto() {
        return PopularRequestDto.builder()
                .searchType("1")
                .yy("2023")
                .mm("04")
                .page(0)
                .build();
    }

    public static PopularResponseDto createPopularResponseDto() {
        return PopularResponseDto.builder()
                .ranking("1")
                .num("5555")
                .title("제목")
                .singer("가수")
                .build();
    }


    public static SearchDto createSearchDto() {
        return SearchDto.builder()
                .searchType(1)
                .page(0)
                .searchKeyWord("아리랑")
                .searchNation("KOR")
                .build();
    }
    public static LoginDto createLoginDto() {
        return LoginDto.builder()
                .oauthAccessToken("testAccessToken")
                .loginType("KAKAO")
                .build();
    }
    public static SearchMusicResponseDto createSearchMusicDto() {
        return SearchMusicResponseDto.builder()
                .musicId("1")
                .num("44444")
                .title("노래 제목")
                .singer("가수")
                .lyricist("작사")
                .composer("작곡")
                .youtubeUrl("YoutubeURL")
                .nation("국가")
                .build();

    }

    public static NewMusicResponseDto createNewMusicDto() {
        return NewMusicResponseDto.builder()
                .num("44444")
                .title("노래 제목")
                .singer("가수")
                .lyricist("작사")
                .composer("작곡")
                .youtubeUrl("YoutubeURL")
                .nation("NEW")
                .build();

    }

    public static PlayListDto createPlayListDto() {
        return PlayListDto.builder()
                .playListId(1L)
                .title("플레이 리스트 이름")
                .color("컬러 코드")
                .emoji("이모지 코드")
                .build();
    }

    public static PlayListModifyRequestDto createPlayListModifyRequestDto() {
        return PlayListModifyRequestDto.builder()
                .playListId(1L)
                .orderList(List.of(2, 1,0))
                .build();
    }

    public static PlayListRequestDto createPlayListRequestDto() {
        return PlayListRequestDto.builder()
                .userId(1L)
                .title("플레이 리스트 이름")
                .color("컬러 코드")
                .emoji("이모지 코드")
                .build();
    }

    public static UserMusicDto createUserMusicDto(Integer orderNum) {
        return UserMusicDto.builder()
                .userMusicId("1")
                .playListId("1")
                .musicId("1")
                .num("44444")
                .title("노래 제목")
                .singer("가수")
                .lyricist("작사")
                .composer("작곡")
                .youtubeUrl("YoutubeURL")
                .nation("국가")
                .orderNum(orderNum)
                .kyNum("72727")
                .mrSound(false)
                .build();
    }

    public static UserDto createUserDto() {
        return UserDto.builder()
                .userId(1L)
                .email("test@test.com")
                .userStatus("VERIFIED")
                .loginType("GOOGLE")
                .build();

    }

    public static User createUser() {
        User get = User.builder()
                .userId(1L)
                .email("test@test.com")
                .userStatus(UserStatus.VERIFIED)
                .loginType(LoginType.KAKAO)
                .roles(JwtAuthorityUtils.USER_ROLES_STRING_CALL)
                .build();
        get.setCreateAt(LocalDateTime.now());
        get.setUpdateAt(LocalDateTime.now());
        return get;
    }

    public static UserMusicRequestDto createUserMusicRequestDto() {
        return UserMusicRequestDto.builder()
                .userId(1L)
                .playListId(2L)
                .musicNum(List.of("4444","5555"))
                .build();
    }

}
