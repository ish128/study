package study.ish.restful.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import study.ish.restful.RestfulApplication;
import study.ish.restful.common.RestDocTestConfiguration;
import study.ish.restful.common.TaskDescription;

import java.time.LocalDateTime;


import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocTestConfiguration.class)
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
        .andExpect(header().exists("Location"))
        .andExpect(header().string("content-type", MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        .andExpect(jsonPath("offline").value(true))
        .andExpect(jsonPath("free").value(false))
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists())
        .andExpect(jsonPath("_links.query-events").exists())
        .andExpect(jsonPath("_links.update-event").exists())
        .andDo(document("create-event",
            links(
                linkWithRel("self").description("link to self"),
                linkWithRel("profile").description("link to profile"),
                linkWithRel("query-events").description("link to query events"),
                linkWithRel("update-event").description("link to update event")
                ),
            requestHeaders(
                headerWithName(HttpHeaders.ACCEPT).description(HttpHeaders.ACCEPT),
                headerWithName(HttpHeaders.CONTENT_TYPE).description(HttpHeaders.CONTENT_TYPE)
                ),
            requestFields(
                fieldWithPath("name").description("이벤트명"),
                fieldWithPath("description").description("이벤트 설명"),
                fieldWithPath("beginEnrollmentDateTime").description("이벤트 등록 시작일"),
                fieldWithPath("closeEnrollmentDateTime").description("이벤트 등록 마감일"),
                fieldWithPath("beginEventDateTime").description("이벤트 시작일"),
                fieldWithPath("endEventDateTime").description("이벤트 종료일"),
                fieldWithPath("location").description("장소"),
                fieldWithPath("basePrice").description("기본가"),
                fieldWithPath("maxPrice").description("최고가"),
                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment")
                ),
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description(HttpHeaders.CONTENT_TYPE)
            ),
            responseFields(
                fieldWithPath("id").description("id"),
                fieldWithPath("offline").description("offline"),
                fieldWithPath("free").description("free"),
                fieldWithPath("eventStatus").description("eventStatus"),
                fieldWithPath("_links.self.href").description("self"),
                fieldWithPath("_links.profile.href").description("profile"),
                fieldWithPath("_links.query-events.href").description("self"),
                fieldWithPath("_links.update-event.href").description("self"),
                fieldWithPath("name").description("이벤트명"),
                fieldWithPath("description").description("이벤트 설명"),
                fieldWithPath("beginEnrollmentDateTime").description("이벤트 등록 시작일"),
                fieldWithPath("closeEnrollmentDateTime").description("이벤트 등록 마감일"),
                fieldWithPath("beginEventDateTime").description("이벤트 시작일"),
                fieldWithPath("endEventDateTime").description("이벤트 종료일"),
                fieldWithPath("location").description("장소"),
                fieldWithPath("basePrice").description("기본가"),
                fieldWithPath("maxPrice").description("최고가"),
                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment")
            )

            ));


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


/*  @Test
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
  }*/

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
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].objectName").exists())
        .andExpect(jsonPath("$[0].defaultMessage").exists());


  }
}
