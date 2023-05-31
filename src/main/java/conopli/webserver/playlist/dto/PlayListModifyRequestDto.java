package conopli.webserver.playlist.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlayListModifyRequestDto {

    private Long playListId;

    private List<Integer> orderList;

}
