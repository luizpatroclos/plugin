package eu.domibus.ep.edelivery.plugin.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ApiError {

    private HttpStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timeStamp;
    private String message;
    private List<String> errors;



    public ApiError(HttpStatus status, LocalDateTime timeStamp, String message, List<String> errors) {
        super();
        this.status = status;
        this.timeStamp = timeStamp;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, LocalDateTime timeStamp, String message, String error) {
        super();
        this.status = status;
        this.timeStamp = timeStamp;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }
}
