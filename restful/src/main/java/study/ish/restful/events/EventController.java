package study.ish.restful.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.ish.restful.common.ErrorResource;
import study.ish.restful.common.StudyException;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static study.ish.restful.common.ErrorType.BAD_REQUEST_DATA;
import static study.ish.restful.common.ErrorType.NOT_FOUND_DATA;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = {MediaTypes.HAL_JSON_UTF8_VALUE})
public class EventController {

  private final EventRepository eventRepository;

  private final ModelMapper modelMapper;

  private final EventValidator eventValidator;

  @PostMapping
  public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }

    Event event = modelMapper.map(eventDto, Event.class);
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

/*  @PostMapping("/{id}")
  public ResponseEntity updateEvent(@RequestBody @Valid EventDto eventDto, Errors errors, @PathVariable("id") Integer id) {
    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().body(errors);
    }

    Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException());
    event.setName(eventDto.getName());
    eventRepository.save(event);

    ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());

    EventResource eventResource = new EventResource(event);
    eventResource.add(selfLinkBuilder.withSelfRel());
    eventResource.add(linkTo(EventController.class).withRel("query-events"));

    return ResponseEntity.ok().body(null);
  }*/

  @GetMapping
  public ResponseEntity queryList(Pageable pageable, PagedResourcesAssembler<Event> assembler) {

    Page<Event> page = eventRepository.findAll(pageable);
    PagedResources<Resource<Event>> pagedResources = assembler.toResource(page, e -> new EventResource(e));

    pagedResources.add(new Link("/docs/index.html#resources-query-events").withRel("profile"));

    return ResponseEntity.ok(pagedResources);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventResource> queryOne(@PathVariable int id) {
    Event event = eventRepository.findById(id).orElseThrow(() -> new StudyException(NOT_FOUND_DATA));
    EventResource eventResource = new EventResource(event);
    eventResource.add(new Link("/docs/index.html#resources-query-event").withRel("profile"));
    return ResponseEntity.ok(eventResource);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateEvent(@RequestBody @Valid EventDto eventDto,
                                    Errors errors,
                                    @PathVariable int id) {

    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      new StudyException(BAD_REQUEST_DATA);
    }
    Event event = eventRepository.findById(id)
        .orElseThrow(() -> new StudyException(NOT_FOUND_DATA));

    modelMapper.map(eventDto, event);
    eventRepository.save(event);

    EventResource eventResource = new EventResource(event);
    eventResource.add(new Link("/docs/index.html#resources-update-event").withRel("profile"));
    return ResponseEntity.ok(eventResource);

  }
}
