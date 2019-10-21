package study.ish.restful.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.IOException;

@JsonComponent
public class EventJsonSerializer extends JsonSerializer<Errors> {

  @Override
  public void serialize(Errors value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

    gen.writeStartArray();

    for(FieldError e : value.getFieldErrors()){
      gen.writeStartObject();
      gen.writeStringField("defaultMessage", e.getDefaultMessage());
      gen.writeStringField("code", e.getCode());
      gen.writeStringField("objectName", e.getObjectName());
      gen.writeStringField("field", e.getField());
      if(e.getRejectedValue()!=null){
        gen.writeStringField("rejectedValue", e.getRejectedValue().toString());
      }

      gen.writeEndObject();
    }
    for(ObjectError e : value.getGlobalErrors()){
      gen.writeStartObject();
      gen.writeStringField("defaultMessage", e.getDefaultMessage());
      gen.writeStringField("code", e.getCode());
      gen.writeStringField("objectName", e.getObjectName());
      gen.writeEndObject();
    }

    gen.writeEndArray();
  }
}
