package study.ish.restful.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration //test에서만 사용하는 설정
public class RestDocTestConfiguration {

  @Bean
  public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer(){
    return configurer -> configurer.operationPreprocessors()
        .withRequestDefaults(prettyPrint())
        .withResponseDefaults(prettyPrint());
  }


}
