package study.ish.restful.events;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


public class EventTest {

  @Test
  public void shouldBuilder(){
    Event event =
        Event.builder()
        .basePrice(1)
        .id(1)
         .build();
    assertThat(event, is(notNullValue()));
  }

  @Test
  public void shouldCreate(){
    int basePrice = 1;
    int id = 1;

    Event event = new Event();
    event.setBasePrice(basePrice);
    event.setId(id);

    assertThat(event.getBasePrice(), is(basePrice));
    assertThat(event.getId(), is(id));
  }
}