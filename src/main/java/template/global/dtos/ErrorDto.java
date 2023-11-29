package template.global.dtos;

import org.springframework.http.HttpStatus;

public class ErrorDto {
    public Integer status;
    public String error;
    public String message;

    public ErrorDto(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }
}
