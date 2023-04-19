package conopli.webserver.utils;

import conopli.webserver.constant.BaseUrl;
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


}
