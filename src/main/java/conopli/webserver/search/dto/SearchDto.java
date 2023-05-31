package conopli.webserver.search.dto;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SearchDto {
    int searchType;
    String searchKeyWord;
    String searchNation;
    int page;
}
