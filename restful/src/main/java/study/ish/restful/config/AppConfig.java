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

import java.util.Set;

@Configuration
public class AppConfig {
  @Bean
  static ApplicationRunner applicationRunner() {
    return new ApplicationRunner() {

      @Autowired
      private AccountService accountService;

      @Override
      public void run(ApplicationArguments args) throws Exception {
        Account account = Account.builder()
            .email("ish128@naver.com")
            .password("test111")
            .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
            .build();
        accountService.saveAccount(account);
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
