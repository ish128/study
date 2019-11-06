package study.ish.restful.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum ErrorType {
  NOT_FOUND_DATA(NOT_FOUND),
  SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_REQUEST_DATA(BAD_REQUEST);

  private final HttpStatus httpStatus;

  ErrorType(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

}
