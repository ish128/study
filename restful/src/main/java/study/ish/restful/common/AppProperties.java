package study.ish.restful.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "my-app")
public class AppProperties {

  @NotEmpty
  private String userName;

  @NotEmpty
  private String userPassword;

  @NotEmpty
  private String adminName;

  @NotEmpty
  private String adminPassword;

  @NotEmpty
  private String clientId;

  @NotEmpty
  private String clientSecret;
}
