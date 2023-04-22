package conopli.webserver.constant;

import lombok.Getter;

public enum BaseUrl {
    BASE_URL("https://zulseoza.site/api/music");


    @Getter
    final String url;

    BaseUrl(String url) {
        this.url = url;
    }

}
