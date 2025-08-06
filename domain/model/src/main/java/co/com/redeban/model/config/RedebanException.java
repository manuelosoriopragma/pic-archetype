package co.com.redeban.model.config;

import lombok.Getter;

@Getter
public class RedebanException extends RuntimeException{
    private final ErrorCode error;

    public RedebanException(ErrorCode error, String message) {
        super(message);
        this.error = error;
    }

    public RedebanException(ErrorCode error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public RedebanException(ErrorCode errorCode) {
        super(errorCode.getLog());
        this.error = errorCode;
    }
}
