package conopli.webserver.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.utils.StubUtils;
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
        LoginDto loginDto = StubUtils.createLoginDto();
        Gson gson = new Gson();
        JsonElement kakaoElement = JsonParser.parseString(getKakaoResponse());
        JsonElement kakaoAccount = kakaoElement.getAsJsonObject().get("kakao_account");
        String kakaoEmail = kakaoAccount.getAsJsonObject().get("email").getAsString();

        JsonElement naverElement = JsonParser.parseString(getNaverResponse());
        JsonElement naverAccount = naverElement.getAsJsonObject().get("response");
        String naverEmail = naverAccount.getAsJsonObject().get("email").getAsString();

        JsonElement googleElement = JsonParser.parseString(getGoogleResponse());
        String googleEmail = googleElement.getAsJsonObject().get("email").getAsString();
        //When
        //Then
        System.out.println(kakaoEmail);
        System.out.println(naverEmail);
        System.out.println(googleEmail);
    }

    private String getKakaoResponse(){
        return "{ \"id\": 2744848480, \"connected_at\": \"2023-04-11T12:51:12Z\", \"properties\": { \"nickname\": \"신승구\" }, \"kakao_account\": { \"profile_nickname_needs_agreement\": false, \"profile\": { \"nickname\": \"신승구\" }, \"has_email\": true, \"email_needs_agreement\": false, \"is_email_valid\": true, \"is_email_verified\": true, \"email\": \"less0805@gmail.com\" } }";
    }

    private String getGoogleResponse() {
        return "{ \"id\": \"110404705596622233442\", \"email\": \"sussa1933@gmail.com\", \"verified_email\": true, \"picture\": \"https://lh3.googleusercontent.com/a/default-user=s96-c\" }";
    }

    private String getNaverResponse() {
        return "{ \"resultcode\": \"00\", \"message\": \"success\", \"response\": { \"id\": \"J4Ky0JqZG--1M4qVew1Mr6LyexbPbzSwPc8mqJdHqps\", \"email\": \"admin@nodemoe.com\", \"name\": \"이수영\" } }";
    }


}