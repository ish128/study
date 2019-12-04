package study.ish.restful.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AppPropertiesTest {

  @Autowired
  private AppProperties appProperties;

  @Test
  public void shouldEqualTo() {
    assertThat(appProperties.getAdminName()).isEqualTo("admin@email.com");
    assertThat(appProperties.getAdminPassword()).isEqualTo("222");
    assertThat(appProperties.getUserName()).isEqualTo("user@email.com");
    assertThat(appProperties.getUserPassword()).isEqualTo("1111");
    assertThat(appProperties.getClientId()).isEqualTo("myApp");
    assertThat(appProperties.getClientSecret()).isEqualTo("pass");

  }
}