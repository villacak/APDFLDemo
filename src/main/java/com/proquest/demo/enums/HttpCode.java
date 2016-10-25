package com.proquest.demo.enums;

/**
 * Created by zliu on 6/29/2016.
 */
public enum HttpCode {
    SUCCESS(200), ERROR(400);

    private int code;

    private HttpCode(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
