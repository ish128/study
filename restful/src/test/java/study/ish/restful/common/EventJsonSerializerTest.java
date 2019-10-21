package study.ish.restful.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Slf4j
@JsonTest
@RunWith(SpringRunner.class)
public class EventJsonSerializerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void test1() throws Exception{
    Errors errors =  new BindException("target", "objectName");
    ((BindException) errors).addError(new FieldError( "objectName", "field", "defaultMessage" ));

    String json = objectMapper.writeValueAsString(errors);

    log.info(json);

    is(jsonPath("$[0].objectName").equals("objectName"));
    is(jsonPath("$[0].defaultMessage").equals("defaultMessage"));
    is(jsonPath("$[0].field").equals("field"));

  }

}