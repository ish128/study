package study.ish.restful.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.ish.restful.accounts.Account;
import study.ish.restful.accounts.AccountRole;
import study.ish.restful.accounts.AccountService;
import study.ish.restful.common.BaseControllerTest;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

  @Autowired
  private AccountService accountService;

  @Test
  public void shouldGetAuthToken() throws Exception {
    String clientId = "myApp";
    String clinetSecret = "pass";

    String username = "popqpq";
    String password = "test128";

    Account account = Account.builder()
        .email(username)
        .password(password)
        .roles(Set.of(AccountRole.USER))
        .build();
    accountService.saveAccount(account);

    mockMvc.perform(post("/oauth/token")
        .with(httpBasic(clientId, clinetSecret))
        .param("username", username)
        .param("password", password)
        .param("grant_type", "password"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("access_token").exists());

  }

}