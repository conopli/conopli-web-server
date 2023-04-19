package conopli.webserver.exception;


import conopli.webserver.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ServiceLogicException extends RuntimeException {

    private ErrorCode errorCode;

    public ServiceLogicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}