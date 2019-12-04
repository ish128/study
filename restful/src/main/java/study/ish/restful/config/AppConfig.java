package study.ish.restful.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.ish.restful.accounts.Account;
import study.ish.restful.accounts.AccountRole;
import study.ish.restful.accounts.AccountService;
import study.ish.restful.common.AppProperties;

import java.util.Set;

@Configuration
public class AppConfig {


  @Bean
  public ApplicationRunner applicationRunner() {
    return new ApplicationRunner() {

      @Autowired
      private AccountService accountService;

      @Autowired
      private AppProperties appProperties;

      @Override
      public void run(ApplicationArguments args) throws Exception {
        Account user = Account.builder()
            .email(appProperties.getUserName())
            .password(appProperties.getUserPassword())
            .roles(Set.of(AccountRole.USER))
            .build();

        Account admin = Account.builder()
            .email(appProperties.getAdminName())
            .password(appProperties.getAdminPassword())
            .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
            .build();

        accountService.saveAccount(user);
        accountService.saveAccount(admin);
      }
    };
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
