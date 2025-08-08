package co.com.redeban.model.config;

import lombok.Getter;

@Getter
public enum ErrorCode {

    E500000("E500-000", "Ha ocurrido un error en el sistema, por favor contacte al administrador"),
    S204000("S204-000", "No data found"),
    B400002("B400-002", "Bad Request-fields bad format"),
    B409001("B409-001", "Bussines error example");



    private final String code;
    private final String log;

    ErrorCode(String code, String log) {
        this.code = code;
        this.log = log;
    }
}
