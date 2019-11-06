package study.ish.restful.events;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import study.ish.restful.common.BaseControllerTest;
import study.ish.restful.common.TaskDescription;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class EventControllerSpringMvcTest extends BaseControllerTest {

  @Autowired
  EventRepository eventRepository;

  @Test
  @TaskDescription("정상")
  public void createEvent() throws Exception {

    EventDto event = EventDto.builder()
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


    this.mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(this.objectMapper.writeValueAsString(event))
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
                headerWithName(CONTENT_TYPE).description(CONTENT_TYPE)
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
                headerWithName(CONTENT_TYPE).description(CONTENT_TYPE)
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
  public void createEvent_badRequest() throws Exception {
    Event event = Event.builder()
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


    this.mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(this.objectMapper.writeValueAsString(event))
    )
        .andDo(print())
        .andExpect(status().isBadRequest());


  }


  @Test
  @TaskDescription("잘못된 요청값")
  public void createEvent_badRequest_worngInput() throws Exception {
    EventDto event = EventDto.builder()
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

    this.mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(this.objectMapper.writeValueAsString(event))
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("content[0].objectName").exists())
        .andExpect(jsonPath("content[0].defaultMessage").exists())
        .andExpect(jsonPath("content[0].code").exists())
        .andExpect(jsonPath("_links.index").exists());
  }

  @Test
  @TaskDescription("30개의 테스트 데이타 생성, 10개 사이즈로 2번째 목록 조회")
  public void queryList_success() throws Exception {
    IntStream.rangeClosed(1, 30).forEach(this::createEvent);
    this.mockMvc.perform(get("/api/events")
        .param("page", "1")
        .param("size", "10")
        .param("sort", "id,ASC")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("_embedded.eventList[0].name").value("event 11"))
        .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
        .andExpect(jsonPath("_embedded.eventList[9].name").value("event 20"))
        .andExpect(jsonPath("_embedded.eventList[9]._links.self").exists())
        .andExpect(jsonPath("_links.next").exists())
        .andExpect(jsonPath("_links.prev").exists())
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists())
        .andDo(document("query-events",
            links(
                linkWithRel("first").description("link to first event of this page"),
                linkWithRel("last").description("link to last  event of this page"),
                linkWithRel("next").description("link to next page"),
                linkWithRel("prev").description("link to prev page"),
                linkWithRel("self").description("link to self page"),
                linkWithRel("profile").description("link to profile page")
            ),
            requestParameters(
                parameterWithName("size").description("size of a page"),
                parameterWithName("sort").description("sort"),
                parameterWithName("page").description("number of the page")
            ),

            responseHeaders(
                headerWithName(CONTENT_TYPE).description(CONTENT_TYPE)
            ),
            responseFields(
                fieldWithPath("_embedded.eventList").description("queried event list"),
                fieldWithPath("_embedded.eventList[].id").description("id"),
                fieldWithPath("_embedded.eventList[].offline").description("offline"),
                fieldWithPath("_embedded.eventList[].free").description("free"),
                fieldWithPath("_embedded.eventList[].eventStatus").description("eventStatus"),
                fieldWithPath("_embedded.eventList[]._links.self.href").description("self"),
                fieldWithPath("_embedded.eventList[].name").description("이벤트명"),
                fieldWithPath("_embedded.eventList[].description").description("이벤트 설명"),
                fieldWithPath("_embedded.eventList[].beginEnrollmentDateTime").description("이벤트 등록 시작일"),
                fieldWithPath("_embedded.eventList[].closeEnrollmentDateTime").description("이벤트 등록 마감일"),
                fieldWithPath("_embedded.eventList[].beginEventDateTime").description("이벤트 시작일"),
                fieldWithPath("_embedded.eventList[].endEventDateTime").description("이벤트 종료일"),
                fieldWithPath("_embedded.eventList[].location").description("장소"),
                fieldWithPath("_embedded.eventList[].basePrice").description("기본가"),
                fieldWithPath("_embedded.eventList[].maxPrice").description("최고가"),
                fieldWithPath("_embedded.eventList[].limitOfEnrollment").description("limitOfEnrollment"),
                fieldWithPath("_links.first.href").description("link to first event of this page"),
                fieldWithPath("_links.last.href").description("link to last  event of this page"),
                fieldWithPath("_links.next.href").description("link to next page"),
                fieldWithPath("_links.prev.href").description("link to prev page"),
                fieldWithPath("_links.self.href").description("link to self page"),
                fieldWithPath("_links.profile.href").description("link to profile page"),
                fieldWithPath("page.size").description("link to profile page"),
                fieldWithPath("page.totalElements").description("link to profile page"),
                fieldWithPath("page.totalPages").description("link to profile page"),
                fieldWithPath("page.number").description("link to profile page")
            )
        ));
  }


  @Test
  public void queryOne() throws Exception {
    Event event = createEvent(100);
    this.mockMvc.perform(get("/api/events/{id}", event.getId()))
        .andDo(print())
        .andExpect(jsonPath("name").exists())
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists())
        .andDo(document("query-event",
            links(
                linkWithRel("self").description("link to self page"),
                linkWithRel("profile").description("link to profile page")
            ),
            responseHeaders(
                headerWithName(CONTENT_TYPE).description(CONTENT_TYPE)
            ),
            responseFields(
                fieldWithPath("id").description("id"),
                fieldWithPath("offline").description("offline"),
                fieldWithPath("free").description("free"),
                fieldWithPath("eventStatus").description("eventStatus"),
                fieldWithPath("_links.self.href").description("self"),
                fieldWithPath("_links.profile.href").description("profile"),
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
  public void queryOneFailed() throws Exception {
    this.mockMvc.perform(get("/api/events/1123213"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("_links.index").exists());
  }

  @Test
  public void updateEventSuccessfully() throws Exception {
    Event event = createEvent(1);
    String updatedName = "test1";

    EventDto eventDto = EventDto.builder()
        .name(updatedName)
        .endEventDateTime(event.getEndEventDateTime())
        .beginEventDateTime(event.getBeginEventDateTime())
        .beginEnrollmentDateTime(event.getBeginEnrollmentDateTime())
        .closeEnrollmentDateTime(event.getCloseEnrollmentDateTime())
        .build();

    this.mockMvc.perform(put("/api/events/{id}", event.getId())
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(this.objectMapper.writeValueAsString(eventDto)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value(updatedName))
        .andDo(document("update-event",
            links(
                linkWithRel("self").description("link to self page"),
                linkWithRel("profile").description("link to profile page")
            ),
            requestHeaders(
                headerWithName(CONTENT_TYPE).description(CONTENT_TYPE),
                headerWithName(ACCEPT).description(ACCEPT)
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
                headerWithName(CONTENT_TYPE).description(CONTENT_TYPE)
            ),
            responseFields(
                fieldWithPath("id").description("id"),
                fieldWithPath("offline").description("offline"),
                fieldWithPath("free").description("free"),
                fieldWithPath("eventStatus").description("eventStatus"),
                fieldWithPath("_links.self.href").description("self"),
                fieldWithPath("_links.profile.href").description("profile"),
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
  public void updateEvent404() throws Exception {
    EventDto eventDto = EventDto.builder()
        .name("ttt")
        .endEventDateTime(LocalDateTime.now())
        .beginEventDateTime(LocalDateTime.now().minusYears(1))
        .beginEnrollmentDateTime(LocalDateTime.now())
        .closeEnrollmentDateTime(LocalDateTime.now().minusMonths(1))
        .build();

    this.mockMvc.perform(put("/api/events/{id}", 100000)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(MediaTypes.HAL_JSON)
        .content(this.objectMapper.writeValueAsString(eventDto)))
        .andDo(print())
        .andExpect(status().isNotFound());
  }


  private Event createEvent(int i) {
    LocalDateTime now = LocalDateTime.now();
    return eventRepository.save(Event.builder()
        .id(i)
        .name("event " + i)
        .beginEnrollmentDateTime(now.minusMonths(1))
        .closeEnrollmentDateTime(now)
        .beginEventDateTime(now.minusYears(1))
        .endEventDateTime(now)
        .build());
  }
}
