package study.hateoas;

import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.UriTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkTest {

  @Test
  public void shouldCreateLink(){
    Link link = new Link("/something");
    assertThat(link.getHref()).isEqualTo("/something");
    assertThat(link.getRel()).isEqualTo(Link.REL_SELF);

    link = new Link("/something", "my-rel");
    assertThat(link.getHref()).isEqualTo("/something");
    assertThat(link.getRel()).isEqualTo("my-rel");
  }


  @Test
  public void shouldCreateTemplatedLink(){
    Link link = new Link("/{segment}/something{?parameter}");
    assertThat(link.isTemplated()).isTrue();
    assertThat(link.getVariableNames()).contains("segment", "parameter");

    Map<String, Object> values = new HashMap<>();
    values.put("segment", "path");
    values.put("parameter", 42);

    assertThat(link.expand(values).getHref())
        .isEqualTo("/path/something?parameter=42");
  }

  @Test
  public void shouldCreateTemplatedLinkAnother(){
    UriTemplate template =
        new UriTemplate("/{segment}/something").with("parameter", VariableType.REQUEST_PARAM);

    assertThat(template.toString()).isEqualTo("/{segment}/something{?parameter}");

    Link link = new Link(template, Link.REL_SELF);

    Map<String, Object> values = new HashMap<>();
    values.put("segment", "path");
    values.put("parameter", 42);

    assertThat(link.expand(values).getHref())
        .isEqualTo("/path/something?parameter=42");
  }
}
