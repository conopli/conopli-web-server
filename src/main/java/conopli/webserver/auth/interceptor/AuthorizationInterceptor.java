package conopli.webserver.auth.interceptor;

import conopli.webserver.auth.token.JwtTokenizer;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.exception.ServiceLogicException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtTokenizer tokenizer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization Interception URL : {}", request.getRequestURL());

        // web, chrome 의 경우 GET, POST, OPTION 통과
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // TODO Header 검증 로직
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new ServiceLogicException(ErrorCode.TOKEN_NOT_NULL);
        }
        String jws = accessToken.replace("Bearer ", "");
        try {
            tokenizer.verifyAccessToken(jws);
        } catch (ServiceLogicException ee) {
            throw ee;
        } catch (Exception e) {
            throw new ServiceLogicException(ErrorCode.ACCESS_DENIED);
        }
        String email = tokenizer.getEmail(jws);
        if (email != null) {
            // RequestContextHolder - 하나의 요청에 생성되는 Context
            RequestAttributes requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("username", email, RequestAttributes.SCOPE_REQUEST);
            return true;
        }
        throw new ServiceLogicException(ErrorCode.UNAUTHORIZED);


    }
}
