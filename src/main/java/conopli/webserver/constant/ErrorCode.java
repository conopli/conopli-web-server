package conopli.webserver.constant;

import lombok.Getter;

public enum ErrorCode {

    BAD_REQUEST(400, "BAD REQUEST", 14001),
    EXIST_USER(400, "Already Exist User Email", 14003),
    EXIST_USER_MUSIC(400, "Already Exist User Music", 14004),
    ARGUMENT_MISMATCH_BAD_REQUEST(400, "Argument Mismatch Bad Request" , 14005),
    ACCESS_DENIED(403, "ACCESS DENIED", 14006),
    INACTIVE_USER(403, "Inactive User", 14007),
    NOT_FOUND(404, "NOT FOUND", 14008),
    NOT_FOUND_REQUEST_URL(404, "Not Found Request Url ", 14009),
    NOT_FOUND_USER(404, "Not Found User", 14010),
    NOT_FOUND_USER_MUSIC(404, "Not Found User Music", 14011),
    NOT_FOUND_PLAY_LIST(404, "Not Found Playlist", 14012),
    EXPIRED_ACCESS_TOKEN(403, "EXPIRED ACCESS TOKEN", 14013),
    TOKEN_NOT_NULL(404, "TOKEN_NOT_NULL", 14014),
    EXPIRED_REFRESH_TOKEN(401,"EXPIRED REFRESH TOKEN", 14015),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", 15001),
    DOMAIN_SERVER_HTTP_REQUEST_IO_ERROR(400, "Music Domain Server Request I/O Error(Bad Request)", 14016),
    HTTP_REQUEST_IO_ERROR(400, "OAuth Server Request I/O Error(Bad Request)" , 14017),
    UNAUTHORIZED(401, "Unauthorized(Required Access Token)" , 14018),
    DATA_ACCESS_ERROR(500, "Data Access Error", 15002),
    NOT_IMPLEMENTED(501,"NOT IMPLEMENTED", 15003);

    @Getter
    private final int status;

    @Getter
    private final int code;

    @Getter
    private final String message;

    ErrorCode(int status, String message, int code) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
