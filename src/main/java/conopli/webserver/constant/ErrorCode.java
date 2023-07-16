package conopli.webserver.constant;

import lombok.Getter;

public enum ErrorCode {

    BAD_REQUEST(400, "BAD REQUEST"),
    EXIST_REQUEST_URL(400, "Already Exist Request Url "),
    EXIST_USER(400, "Already Exist User Email"),
    EXIST_USER_MUSIC(400, "Already Exist User Music"),
    ARGUMENT_MISMATCH_BAD_REQUEST(400, "Argument Mismatch Bad Request" ),
    ACCESS_DENIED(403, "ACCESS DENIED"),
    INACTIVE_USER(403, "Inactive User"),
    NOT_FOUND(404, "NOT FOUND"),
    NOT_FOUND_REQUEST_URL(404, "Not Found Request Url "),
    NOT_FOUND_USER(404, "Not Found User"),
    NOT_FOUND_USER_MUSIC(404, "Not Found User Music"),
    NOT_FOUND_PLAY_LIST(404, "Not Found Playlist"),
    EXPIRED_ACCESS_TOKEN(403, "EXPIRED ACCESS TOKEN"),
    TOKEN_NOT_NULL(404, "TOKEN_NOT_NULL"),
    EXPIRED_REFRESH_TOKEN(401,"EXPIRED REFRESH TOKEN"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error" ),
    DOMAIN_SERVER_HTTP_REQUEST_IO_ERROR(400, "Music Domain Server Request I/O Error(Bad Request)" ),
    HTTP_REQUEST_IO_ERROR(400, "OAuth Server Request I/O Error(Bad Request)" ),
    DATA_ACCESS_ERROR(500, "Data Access Error"),
    NOT_IMPLEMENTED(501,"NOT IMPLEMENTED");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ErrorCode(int code, String message) {
        this.status = code;
        this.message = message;
    }

}
