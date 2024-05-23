package ru.javawebinar.topjava.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String typeMessage;
    private String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String[] details) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = details;
    }

    public void setDetails(String[] details) {
        this.details = details;
    }
}