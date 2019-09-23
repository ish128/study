package study.ish.restful.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = {MediaTypes.HAL_JSON_UTF8_VALUE})
public class EventController {

  private  final EventRepository eventRepository;

  private final ModelMapper modelMapper;

  private final EventValidator eventValidator;

  @PostMapping
  public ResponseEntity createEvent(@RequestBody @Valid  EventDto eventDto, Errors errors){
//    URI uri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
    eventValidator.validate(eventDto, errors);

    if(errors.hasErrors()){
      return ResponseEntity.badRequest().body("error");
    }

    Event event =  modelMapper.map(eventDto, Event.class);
    Event newEvent = eventRepository.save(event);
    URI uri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
    return ResponseEntity.created(uri).body(newEvent);
  }
}
