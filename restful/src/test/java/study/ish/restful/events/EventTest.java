package study.ish.restful.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


@RunWith(JUnitParamsRunner.class)
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

  /*
  * JUnitParams의 @Parameters 을 이용해서 입력값을 다르게 해서 여러번 테스트 할 수 있다.
  * 1. 직접 입력 파라미터를 문자열로 주는 방법
  * */
  @Test
  @Parameters({
      "0, 0, true"
      ,"100, 0, false"
      ,"0, 100, false"})
  public void testFree1(int basePrice, int maxPrice, boolean isFree){
    Event event = Event.builder()
        .basePrice(basePrice)
        .maxPrice(maxPrice)
        .build();

    event.update();

    assertThat(event.isFree(), is(isFree));
  }


  /*
   * JUnitParams의 @Parameters 을 이용해서 입력값을 다르게 해서 여러번 테스트 할 수 있다.
   * 2. 입력 파라미터를 method 명으로 지정할 수 있다. 해당 메소드는 입력파라미터를 리턴해야한다.
   * */
  @Test
  @Parameters(method = "paramsForTestFree2")
  public void testFree2(int basePrice, int maxPrice, boolean isFree){
    Event event = Event.builder()
        .basePrice(basePrice)
        .maxPrice(maxPrice)
        .build();

    event.update();

    assertThat(event.isFree(), is(isFree));
  }

  private Object[] paramsForTestFree2(){
    return new Object[]{
        new Object[]{0,0,true},
        new Object[]{100,0,false},
        new Object[]{0,100,false},
        new Object[]{100,100,false},
    };
  }

  /*
   * JUnitParams의 @Parameters 을 이용해서 입력값을 다르게 해서 여러번 테스트 할 수 있다.
   * 3. 입력 파라미터의 method명을 관례에 따라 명명하면, 직접 지정하지 않아도 알아서 찾는다.
   *  parametersForTestFree3
   *  parametersFor테스트메소드
   * */
 @Test
 @Parameters
 public void testFree3(int basePrice, int maxPrice, boolean isFree){
   Event event = Event.builder()
       .basePrice(basePrice)
       .maxPrice(maxPrice)
       .build();

   event.update();

   assertThat(event.isFree(), is(isFree));
 }

  private Object[] parametersForTestFree3(){
    return new Object[]{
        new Object[]{0,0,true},
        new Object[]{100,0,false},
        new Object[]{0,100,false},
        new Object[]{100,100,false},
    };
  }

  @Parameters
  @Test
  public void testOffline(String location, boolean isOffline){
    Event event = Event.builder()
        .location(location)
        .build();
    event.update();

    assertThat(event.isOffline(), is(isOffline));


    event = Event.builder().build();
    event.update();

    assertThat(event.isOffline(), is(false));

  }

  public Object[] parametersForTestOffline(){
    return new Object[]{
        new Object[]{null,false},
        new Object[]{"",false},
        new Object[]{" ",false},
        new Object[]{"starbucks",true}
    };
  }
}