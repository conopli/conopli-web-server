package conopli.webserver.utils;

import conopli.webserver.constant.BaseUrl;
import conopli.webserver.map.dto.MapSearchDto;
import conopli.webserver.search.dto.PopularRequestDto;
import conopli.webserver.search.dto.SearchDto;
import org.springframework.stereotype.Component;

@Component
public class UrlCreateUtil {

    public static String createSearchRequestUrl(SearchDto dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.BASE_URL.getUrl());
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

    public static String createSearchByNumRequestUrl(String musicNum) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.BASE_URL.getUrl());
        sb.append("/search/");
        sb.append(musicNum);
        return sb.toString();
    }

    public static String createPopularRequestUrl(PopularRequestDto dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.BASE_URL.getUrl());
        sb.append("/popular");
        sb.append("?searchType=");
        sb.append(dto.getSearchType());
        sb.append("&syy=");
        sb.append(dto.getSyy());
        sb.append("&smm=");
        sb.append(dto.getSmm());
        sb.append("&eyy=");
        sb.append(dto.getEyy());
        sb.append("&emm=");
        sb.append(dto.getEmm());

        return sb.toString();
    }

    public static String createNewMusicRequestUrl() {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.BASE_URL.getUrl());
        sb.append("/new-music");
        return sb.toString();
    }

    public static String createKakaoMapRequestUrl(MapSearchDto dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.KAKAO_MAP.getUrl());
        sb.append("?y=");
        sb.append(dto.getLat());
        sb.append("&x=");
        sb.append(dto.getLng());
        sb.append("&radius=");
        sb.append("1000");
        sb.append("&query=");
        sb.append(dto.getSearchType());
        sb.append("&size=");
        sb.append("15");

        return sb.toString();
    }


    public static String createGoogleLoginRequestUrl(String token) {
        StringBuffer sb = new StringBuffer();
        sb.append(BaseUrl.GOOGLE_LOGIN.getUrl());
        sb.append(token);
        return sb.toString();
    }

    public static String createKakaoLoginRequestUrl() {
        return BaseUrl.KAKAO_LOGIN.getUrl();
    }

    public static String createNaverLoginRequestUrl() {
        return BaseUrl.NAVER_LOGIN.getUrl();
    }

}
