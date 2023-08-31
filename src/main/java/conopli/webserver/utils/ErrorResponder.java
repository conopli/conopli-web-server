package conopli.webserver.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import conopli.webserver.constant.ErrorCode;
import conopli.webserver.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ErrorResponder {
    public static void sendErrorResponse(
            HttpServletResponse response,
            Object status) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        if (status instanceof HttpStatus) {
            if (status.equals(HttpStatus.UNAUTHORIZED)) {
                errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED);
            } else if (status.equals(HttpStatus.FORBIDDEN)) {
                errorResponse = ErrorResponse.of(ErrorCode.ACCESS_DENIED);
            } else {
                errorResponse = ErrorResponse.of((HttpStatus) status);
            }
        } else if (status instanceof ErrorCode) {
            errorResponse = ErrorResponse.of((ErrorCode) status);
        }

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.valueOf(errorResponse.getStatus()).value());
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }

}
