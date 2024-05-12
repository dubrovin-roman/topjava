package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR ("error.app"),
    DATA_NOT_FOUND ("error.data.not.found"),
    DATA_ERROR ("error.data"),
    VALIDATION_ERROR ("error.validation");

    private final String typeMessage;

    ErrorType(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getTypeMessage() {
        return typeMessage;
    }
}
