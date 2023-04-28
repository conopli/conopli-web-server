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


}