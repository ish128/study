package study.ish.restful.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = {MediaTypes.HAL_JSON_UTF8_VALUE})
public class EventController {

  private  final EventRepository eventRepository;

  @PostMapping
  public ResponseEntity createEvent(@RequestBody Event event){
//    URI uri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();

    Event newEvent = eventRepository.save(event);
    URI uri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
    return ResponseEntity.created(uri).body(newEvent);
  }
}
