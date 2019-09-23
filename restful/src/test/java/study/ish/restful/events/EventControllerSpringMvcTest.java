package study.ish.restful.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import study.ish.restful.common.TaskDescription;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerSpringMvcTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;


  @Test
  @TaskDescription("정상")
  public void createEvent() throws  Exception{
    EventDto event  = EventDto.builder()
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


    mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(event))
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        .andExpect(jsonPath("offline").value(Matchers.not(true)))
        .andExpect(jsonPath("free").value(Matchers.not(true)))
    .andExpect(header().exists("Location"))
    .andExpect(header().string("content-type", MediaTypes.HAL_JSON_UTF8_VALUE));


  }

  @Test
  @TaskDescription("잘못된 요청")
  public void createEvent_badRequest() throws  Exception{
    Event event  = Event.builder()
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
        .id(100)
        .offline(false)
        .free(false)
        .eventStatus(EventStatus.BEGAN_ENROLLMENT)
        .build();


    mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(objectMapper.writeValueAsString(event))
    )
        .andDo(print())
        .andExpect(status().isBadRequest());


  }


  @Test
  @TaskDescription("빈값으로 요청")
  public void createEvent_badRequest_emptyInput() throws  Exception{
    EventDto event  = EventDto.builder().build();
    mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(objectMapper.writeValueAsString(event))
    )
        .andDo(print())
        .andExpect(status().isBadRequest());


  }

  @Test
  @TaskDescription("잘못된 요청값")
  public void createEvent_badRequest_worngInput() throws  Exception{
    EventDto event  = EventDto.builder()
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 23, 14, 21))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
        .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
        .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
        .basePrice(500)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타텁 팩토리")
        .build();

    mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(objectMapper.writeValueAsString(event))
    )
        .andDo(print())
        .andExpect(status().isBadRequest());


  }
}
