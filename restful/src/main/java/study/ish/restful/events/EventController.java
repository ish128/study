package study.ish.restful.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;

import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
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
    eventValidator.validate(eventDto, errors);
    if(errors.hasErrors()){
      return ResponseEntity.badRequest().body(errors);
    }

    Event event =  modelMapper.map(eventDto, Event.class);
    event.update();

    Event newEvent = eventRepository.save(event);

    ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
    URI uri = selfLinkBuilder.toUri();

    EventResource eventResource = new EventResource(newEvent);
    eventResource.add(linkTo(EventController.class).withRel("query-events"));
    eventResource.add(selfLinkBuilder.withRel("update-event"));
    eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

    return ResponseEntity.created(uri).body(eventResource);
  }

  @PostMapping("/{id}")
  public ResponseEntity updateEvent(@RequestBody @Valid  EventDto eventDto, Errors errors, @PathVariable("id")  Integer id){
    eventValidator.validate(eventDto, errors);
    if(errors.hasErrors()){
      return ResponseEntity.badRequest().body(errors);
    }

    Event event = eventRepository.findById(id).orElseThrow(()-> new RuntimeException());
    event.setName(eventDto.getName());
    eventRepository.save(event);

    ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());

    EventResource eventResource = new EventResource(event);
    eventResource.add(selfLinkBuilder.withSelfRel());
    eventResource.add(linkTo(EventController.class).withRel("query-events"));

    return ResponseEntity.ok().body(null);
  }
}
