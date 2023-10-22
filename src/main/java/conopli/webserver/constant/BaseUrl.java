package conopli.webserver.constant;

import lombok.Getter;

@Getter
public enum BaseUrl {

    KAKAO_MAP("https://dapi.kakao.com/v2/local/search/keyword.json"),
    GOOGLE_LOGIN("https://www.googleapis.com/oauth2/v1/userinfo?access_token="),
    KAKAO_LOGIN("https://kapi.kakao.com/v2/user/me"),
    NAVER_LOGIN("https://openapi.naver.com/v1/nid/me");


    final String url;

    BaseUrl(String url) {
        this.url = url;
    }

}
