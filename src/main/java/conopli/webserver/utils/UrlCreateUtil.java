package conopli.webserver.utils;

import conopli.webserver.config.MusicDomainConfig;
import conopli.webserver.constant.BaseUrl;
import conopli.webserver.map.dto.MapSearchDto;
import conopli.webserver.search.dto.PopularRequestDto;
import conopli.webserver.search.dto.SearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UrlCreateUtil {

    private final MusicDomainConfig musicDomain;

    public String createSearchRequestUrl(SearchDto dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(musicDomain.getBaseUrl());
        sb.append("/search");
        sb.append("?searchType=");
        sb.append(dto.getSearchType());
        sb.append("&searchKeyWord=");
        sb.append(dto.getSearchKeyWord());
        sb.append("&searchNation=");
        sb.append(dto.getSearchNation());
        sb.append("&page=");
        sb.append(dto.getPage());

        return sb.toString();
    }

    public String createSearchByNumRequestUrl(String musicNum) {
        StringBuffer sb = new StringBuffer();
        sb.append(musicDomain.getBaseUrl());
        sb.append("/search/");
        sb.append(musicNum);
        return sb.toString();
    }

    public String createPopularRequestUrl(PopularRequestDto dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(musicDomain.getBaseUrl());
        sb.append("/popular");
        sb.append("?searchType=");
        sb.append(dto.getSearchType());
        sb.append("&yy=");
        sb.append(dto.getYy());
        sb.append("&mm=");
        sb.append(dto.getMm());
        sb.append("&page=");
        sb.append(dto.getPage());

        return sb.toString();
    }

    public String createNewMusicRequestUrl(String yy, String mm, int page) {
        StringBuffer sb = new StringBuffer();
        sb.append(musicDomain.getBaseUrl());
        sb.append("/new-music");
        sb.append("?yy=").append(yy);
        sb.append("&mm=").append(mm);
        sb.append("&page=").append(page);
        return sb.toString();
    }

    public String createKakaoMapRequestUrl(MapSearchDto dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.KAKAO_MAP.getUrl());
        sb.append("?y=");
        sb.append(dto.getLat());
        sb.append("&x=");
        sb.append(dto.getLng());
        sb.append("&radius=");
        sb.append("2000");
        sb.append("&query=");
        sb.append(dto.getSearchType());
        sb.append("&size=");
        sb.append("15");

        return sb.toString();
    }


    public String createGoogleLoginRequestUrl(String token) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.GOOGLE_LOGIN.getUrl());
        sb.append(token);
        return sb.toString();
    }

    public String createKakaoLoginRequestUrl() {
        return BaseUrl.KAKAO_LOGIN.getUrl();
    }

    public String createNaverLoginRequestUrl() {
        return BaseUrl.NAVER_LOGIN.getUrl();
    }

}
