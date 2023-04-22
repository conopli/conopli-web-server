package conopli.webserver.service;

import conopli.webserver.dto.HttpClientPageDto;
import conopli.webserver.search.dto.SearchDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class HttpClientServiceTest {

    @Autowired
    HttpClientService service;

    @Test
    @DisplayName("")
    void test() {
        //Given
        SearchDto dto = SearchDto
                .builder()
                .searchType(1)
                .searchNation("KOR")
                .searchKeyWord("아리랑")
                .page(1)
                .build();
        //When
        HttpClientPageDto httpClientPageDto = service.generateModelPostRequest(dto);
        System.out.println(httpClientPageDto.getData());

        //Then
    }


}