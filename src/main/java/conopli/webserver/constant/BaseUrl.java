package conopli.webserver.constant;

import lombok.Getter;

public enum BaseUrl {
    BASE_URL("https://zulseoza.site/api/music"),
    KAKAO_MAP("https://dapi.kakao.com/v2/local/search/keyword.json");


    @Getter
    final String url;

    BaseUrl(String url) {
        this.url = url;
    }

}
