package conopli.webserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import conopli.webserver.auth.dto.LoginDto;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.dto.HttpClientDto;
import conopli.webserver.dto.HttpClientKakaoMapDto;
import conopli.webserver.dto.HttpClientPageDto;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.map.dto.MapSearchDto;
import conopli.webserver.search.dto.PopularRequestDto;
import conopli.webserver.search.dto.SearchDto;
import conopli.webserver.utils.UrlCreateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HttpClientService {

    @Value("${KAKAO_API_ACCESS_TOKEN}")
    private String kakaoAccessKey;

    private final ObjectMapper mapper = new ObjectMapper();

    private final UrlCreateUtil urlCreateUtil;

    public HttpClientPageDto generateSearchMusicRequest(SearchDto dto) {
        SearchDto searchDto = verifySearchDto(dto);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlCreateUtil.createSearchRequestUrl(searchDto));
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            log.info("Executing request = {} ", httpGet.getRequestLine());
            HttpClientPageDto execute = (HttpClientPageDto) httpclient.execute(httpGet, getResponseHandler());
            return execute;
        } catch (IOException e) {
            throw new ServiceLogicException(ErrorCode.DOMAIN_SERVER_HTTP_REQUEST_IO_ERROR);
        }
    }

    public HttpClientDto generateSearchMusicByNumRequest(String musicNum) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlCreateUtil.createSearchByNumRequestUrl(musicNum));
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            log.info("Executing request = {} ", httpGet.getRequestLine());
            HttpClientDto execute = (HttpClientDto) httpclient.execute(httpGet, getResponseHandler());
            return execute;
        } catch (IOException e) {
            throw new ServiceLogicException(ErrorCode.DOMAIN_SERVER_HTTP_REQUEST_IO_ERROR);
        }
    }

    public HttpClientDto generatePopularMusicRequest(PopularRequestDto dto) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            //Todo 달 변경으로 인해 정상 파싱 되지 않음 임시 조치
            dto.setSmm(String.format("%02d", Integer.parseInt(dto.getSmm())-1));
            dto.setEmm( String.format("%02d", Integer.parseInt(dto.getEmm())-1));
            HttpGet httpGet = new HttpGet(urlCreateUtil.createPopularRequestUrl(dto));
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            log.info("Executing request = {} ", httpGet.getRequestLine());
            HttpClientDto execute = (HttpClientDto) httpclient.execute(httpGet, getResponseHandler());
            return execute;
        } catch (IOException e) {
            throw new ServiceLogicException(ErrorCode.DOMAIN_SERVER_HTTP_REQUEST_IO_ERROR);
        }
    }

    public HttpClientDto generateNewMusicRequest() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlCreateUtil.createNewMusicRequestUrl());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            log.info("Executing request = {} ", httpGet.getRequestLine());
            HttpClientDto execute = (HttpClientDto) httpclient.execute(httpGet, getResponseHandler());
            return execute;
        } catch (IOException e) {
            throw new ServiceLogicException(ErrorCode.DOMAIN_SERVER_HTTP_REQUEST_IO_ERROR);
        }
    }

    public HttpClientKakaoMapDto generateKakaoMapRequest(MapSearchDto dto) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(urlCreateUtil.createKakaoMapRequestUrl(dto));
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpGet.setHeader(HttpHeaders.AUTHORIZATION,kakaoAccessKey);
            log.info("Executing request = {} ", httpGet.getRequestLine());
            log.info("Authorization = {} ", kakaoAccessKey);
            HttpClientKakaoMapDto execute = (HttpClientKakaoMapDto) httpclient.execute(httpGet, getResponseHandler());
            return execute;
        } catch (IOException e) {
            throw new ServiceLogicException(ErrorCode.HTTP_REQUEST_IO_ERROR);
        }
    }

    public String generateLoginRequest(LoginDto dto) {
        String loginType = dto.getLoginType();
        String requestUrl;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            if (loginType.equals("KAKAO")) {
                log.info("Kakao Login Request");
                log.info("Kakao Login Token = {}", dto.getOauthAccessToken());
                requestUrl = urlCreateUtil.createKakaoLoginRequestUrl();
                HttpGet httpGet = new HttpGet(requestUrl);
                httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                httpGet.setHeader(HttpHeaders.AUTHORIZATION,"Bearer "+dto.getOauthAccessToken());
                return (String) httpclient.execute(httpGet, getLoginHandler(loginType));
            } else if (loginType.equals("NAVER")) {
                log.info("Naver Login Request");
                log.info("Naver Login Token = {}", dto.getOauthAccessToken());
                requestUrl = urlCreateUtil.createNaverLoginRequestUrl();
                HttpGet httpGet = new HttpGet(requestUrl);
                httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                httpGet.setHeader("Authorization","Bearer "+dto.getOauthAccessToken());
                return (String) httpclient.execute(httpGet, getLoginHandler(loginType));
            } else {
                log.info("Google Login Request");
                log.info("Google Login Token = {}", dto.getOauthAccessToken());
                requestUrl = urlCreateUtil.createGoogleLoginRequestUrl(dto.getOauthAccessToken());
                HttpGet httpGet = new HttpGet(requestUrl);
                return (String) httpclient.execute(httpGet, getLoginHandler(loginType));
            }
        } catch (IOException e) {
            throw new ServiceLogicException(ErrorCode.HTTP_REQUEST_IO_ERROR);
        }
    }
    private ResponseHandler<?> getLoginHandler(String loginType) {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity responseBody = response.getEntity();
                String res = EntityUtils.toString(responseBody);
                if (loginType.equals("KAKAO")) {
                    JsonElement kakaoElement = JsonParser.parseString(res);
                    JsonElement kakaoAccount = kakaoElement.getAsJsonObject().get("kakao_account");
                    String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
                    return email;
                } else if (loginType.equals("NAVER")) {
                    JsonElement naverElement = JsonParser.parseString(res);
                    JsonElement naverAccount = naverElement.getAsJsonObject().get("response");
                    String email = naverAccount.getAsJsonObject().get("email").getAsString();
                    return email;
                } else {
                    JsonElement googleElement = JsonParser.parseString(res);
                    String email = googleElement.getAsJsonObject().get("email").getAsString();
                    log.info("Google Email Account = {}", email);
                    return email;
                }

            } else {
                //Todo : Status Code 활용하여 예외처리
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }

    private SearchDto verifySearchDto(SearchDto dto) {
        List<Integer> searchType = List.of(1, 2, 4, 8, 16);
        List<String> searchNation = List.of("KOR", "ENG", "JPN");

        boolean searchTypeBol = searchType.contains(dto.getSearchType());
        boolean searchNationBol = searchNation.contains(dto.getSearchNation());
        if (!searchTypeBol || !searchNationBol) {
            throw new ServiceLogicException(ErrorCode.ARGUMENT_MISMATCH_BAD_REQUEST);
        }
        String searchKeyWord = dto.getSearchKeyWord();
        String newKeyWord = searchKeyWord.replaceAll(" ", "%20");
        dto.setSearchKeyWord(newKeyWord);
        return dto;
    }

    private void verifyPopularRequestDto(PopularRequestDto dto) {
        List<Integer> searchType = List.of(1, 2, 3);
        if (!searchType.contains(dto.getSearchType())) {
            throw new ServiceLogicException(ErrorCode.ARGUMENT_MISMATCH_BAD_REQUEST);
        }
    }



    private ResponseHandler<?> getResponseHandler() {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity responseBody = response.getEntity();
                //Todo : 예외가 발생 이유 확인 - response를 두번 읽어들이면 예외 발생
                String res = EntityUtils.toString(responseBody);
                System.out.println(res);
                if (res.contains("pageInfo")) {
                    return mapper.readValue(
                            res,
                            HttpClientPageDto.class
                    );
                } else if(res.contains("documents")) {
                    return mapper.readValue(
                            res,
                            HttpClientKakaoMapDto.class
                    );
                } else {
                    // Todo: 응답 객체 추가
                    return mapper.readValue(
                            res,
                            HttpClientDto.class);
                }

            } else {
                //Todo : Status Code 활용하여 예외처리
                HttpEntity responseBody = response.getEntity();
                String res = EntityUtils.toString(responseBody);
                log.error("Error Response = {}", res);
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }
}
