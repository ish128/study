package study.ish.restful.common;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class StudyExceptionHandler extends BaseExceptionHandler {

  public StudyExceptionHandler() {
    registerExceptionMapping(MethodArgumentNotValidException.class, BAD_REQUEST);
    registerExceptionMapping(HttpMessageNotReadableException.class, BAD_REQUEST);

  }
}
