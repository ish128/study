package study.ish.restful.common;

import lombok.Getter;
import lombok.Setter;

@Getter
public class StudyException extends  RuntimeException {

  private ErrorType errorType;

  public StudyException(ErrorType errorType) {
    super(errorType.name());
    this.errorType = errorType;
  }


}
