package conopli.webserver.resolver;

import conopli.webserver.annotation.UserSession;
import conopli.webserver.user.dto.UserDto;
import conopli.webserver.user.entity.User;
import conopli.webserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
// Resolver는 Config에서 등록 해줘야 한다
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 지원하는 파라미터 , 어노테이션 체크
        // AOP 방식으로 실행하기 위한 리졸버
        boolean annotation = parameter.hasParameterAnnotation(UserSession.class);
        boolean parameterType = parameter.getParameterType().equals(User.class);

        return annotation && parameterType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        // support parameter 에서 true 반환시 여기 실행
        // 사용자 정보 셋팅
        RequestAttributes requestContext = RequestContextHolder.getRequestAttributes();
        assert requestContext != null;
        Object username = requestContext.getAttribute("username", RequestAttributes.SCOPE_REQUEST);
        User user = userService.verifiedUserByEmail((String) username);
        return UserDto.of(user);
    }
}