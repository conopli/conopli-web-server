package conopli.webserver.search.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PopularDto {

    // 1 = 가요 2 = POP 3 = J-POP
    String searchType;

    String syy;
    String smm;

    String eyy;
    String emm;


}
