package conopli.webserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
// RequestContextHolder 데이터를 가져와 Data Parameter 매핑됨 (UserSessionResolver 클래스 확인)
public @interface UserSession {
}
