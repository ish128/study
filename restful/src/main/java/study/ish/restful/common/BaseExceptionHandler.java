package study.ish.restful.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public abstract class BaseExceptionHandler {
  private final HttpStatus defaultErrorStatus = INTERNAL_SERVER_ERROR;
  private final Map<Class, HttpStatus> exceptionMappings = new ConcurrentHashMap<>();

  protected void registerExceptionMapping(
      Class<?> clazz,
      HttpStatus httpStatus) {
    exceptionMappings.put(clazz, httpStatus);
  }


  @ExceptionHandler(StudyException.class)
  @ResponseBody
  public ResponseEntity<ExceptionResource> handleValidationExceptions(
      StudyException ex) {

    HttpStatus status = ex.getErrorType().getHttpStatus();
    ExceptionResponseData exceptionResponseData =
        ExceptionResponseData.builder()
            .code(ex.getMessage())
            .message(ex.getMessage())
            .build();
    return ResponseEntity.status(status).body(new ExceptionResource(exceptionResponseData));
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ExceptionResource> handleValidationExceptions(
      Exception ex) {
    HttpStatus status = exceptionMappings.getOrDefault(ex.getClass(), defaultErrorStatus);
    ExceptionResponseData exceptionResponseData =
        ExceptionResponseData.builder()
            .code(ex.getMessage())
            .message(ex.getMessage())
            .build();
    return ResponseEntity.status(status).body(new ExceptionResource(exceptionResponseData));
  }


  @ExceptionHandler(Throwable.class)
  @ResponseBody
  public ResponseEntity handleThrowable(Throwable ex) {
    HttpStatus status = exceptionMappings.getOrDefault(ex.getClass(), defaultErrorStatus);
    ExceptionResponseData exceptionResponseData =
        ExceptionResponseData.builder()
            .code(ex.getMessage())
            .message(ex.getMessage())
            .build();
    return ResponseEntity.status(status).body(new ExceptionResource(exceptionResponseData));

  }


}
