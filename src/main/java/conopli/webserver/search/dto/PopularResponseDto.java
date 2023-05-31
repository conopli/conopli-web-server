package conopli.webserver.search.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PopularResponseDto {

    public String ranking;

    public String num;

    public String title;

    public String singer;

}
