package study.ish.restful.common;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;
import study.ish.restful.index.IndexController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ErrorResource extends Resource<Errors> {

  public ErrorResource(Errors content, Link... links) {
    super(content, links);
    add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
  }
}
