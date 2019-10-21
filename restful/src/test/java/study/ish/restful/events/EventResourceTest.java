package study.ish.restful.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@JsonTest
@RunWith(SpringRunner.class)
public class EventResourceTest {

  @Autowired
  ObjectMapper objectMapper;

  @Test
  public void test() throws Exception{

    Event  event  = Event.builder()
        .name("Spring")
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
        .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
        .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
        .basePrice(100)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타텁 팩토리")
        .build();
    EventResource eventResource = new EventResource(event);
    eventResource.add(
        linkTo(EventController.class)
            .slash(event.getId())
            .withRel(Link.REL_SELF));

    log.info(objectMapper.writeValueAsString(eventResource));

    assertEquals("{\"id\":null,\"name\":\"Spring\",\"description\":\"REST API Development with Spring\",\"beginEnrollmentDateTime\":\"2018-11-23T14:21:00\",\"closeEnrollmentDateTime\":\"2018-11-24T14:21:00\",\"beginEventDateTime\":\"2018-11-25T14:21:00\",\"endEventDateTime\":\"2018-11-26T14:21:00\",\"location\":\"강남역 D2 스타텁 팩토리\",\"basePrice\":100,\"maxPrice\":200,\"limitOfEnrollment\":100,\"offline\":false,\"free\":false,\"eventStatus\":\"DRAFT\",\"links\":[{\"rel\":\"self\",\"href\":\"/api/events\",\"hreflang\":null,\"media\":null,\"title\":null,\"type\":null,\"deprecation\":null},{\"rel\":\"self\",\"href\":\"/api/events\",\"hreflang\":null,\"media\":null,\"title\":null,\"type\":null,\"deprecation\":null}]}", objectMapper.writeValueAsString(eventResource), true);

  }
}