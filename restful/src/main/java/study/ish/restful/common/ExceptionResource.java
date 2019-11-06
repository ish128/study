package study.ish.restful.common;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import study.ish.restful.index.IndexController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ExceptionResource extends Resource<ExceptionResponseData> {


  public ExceptionResource(ExceptionResponseData content, Link... links) {
    super(content, links);
    add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
  }
}
