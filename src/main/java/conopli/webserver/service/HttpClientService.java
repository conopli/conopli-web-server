package conopli.webserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.dto.HttpClientPageDto;
import conopli.webserver.exception.ServiceLogicException;
import conopli.webserver.search.dto.SearchDto;
import conopli.webserver.utils.UrlCreateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class HttpClientService {

    private final ObjectMapper mapper = new ObjectMapper();

    public HttpClientPageDto generateModelPostRequest(SearchDto dto) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(UrlCreateUtil.createSearchRequestUrl(dto));
            httpGet.setHeader("Content-type", "application/json");
            log.info("Executing request = {} ", httpGet.getRequestLine());
            HttpClientPageDto execute = (HttpClientPageDto) httpclient.execute(httpGet, getResponseHandler());
            return execute;
        } catch (IOException e) {
            throw new ServiceLogicException(ErrorCode.HTTP_REQUEST_IO_ERROR);
        }
    }


    private ResponseHandler<?> getResponseHandler() {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity responseBody = response.getEntity();
                //Todo : 예외가 발생 이유 확인 - response를 두번 읽어들이면 예외 발생
//                    log.info("HTTP ResponseBody = {}",EntityUtils.toString(responseBody));
                String res = EntityUtils.toString(responseBody);
                if (res.contains("pageInfo")) {
                    return mapper.readValue(
                            res,
                            HttpClientPageDto.class
                    );
                } else
                    // Todo: 응답 객체 추가
                    return null;
            } else {
                //Todo : Status Code 활용하여 예외처리
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }
}
