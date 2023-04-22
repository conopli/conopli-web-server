package conopli.webserver.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchDto {
    int searchType;
    int page;
    String searchKeyWord;
    String searchNation;
}
