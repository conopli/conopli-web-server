package conopli.webserver.search.dto;


import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PopularRequestDto {

    // 1 = 가요 2 = POP 3 = J-POP
    String searchType;

    String syy;
    String smm;

    String eyy;
    String emm;


}
