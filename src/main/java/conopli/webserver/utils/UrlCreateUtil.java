package conopli.webserver.utils;

import conopli.webserver.constant.BaseUrl;
import conopli.webserver.map.dto.MapSearchDto;
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

}
