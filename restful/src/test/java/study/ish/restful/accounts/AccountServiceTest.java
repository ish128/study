package study.ish.restful.accounts;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import study.ish.restful.common.AppProperties;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  private AccountService accountService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AppProperties appProperties;


  @Test
  public void findUserByUsername() {

    UserDetails userDetails = accountService.loadUserByUsername(appProperties.getUserName());

    assertThat(passwordEncoder.matches(appProperties.getUserPassword(), userDetails.getPassword()), is(true));


  }

  @Test(expected = UsernameNotFoundException.class)
  public void failfindUserByUsername1() {
    UserDetails userDetails = accountService.loadUserByUsername("test");

  }

  @Test
  public void failfindUserByUsername2() {
    String username = "test.ee";
    try {
      accountService.loadUserByUsername(username);
      fail("supposed to be failed.");
    } catch (UsernameNotFoundException e) {
      assertThat(e.getMessage(), containsString(username));
    }

  }

  @Test
  public void failfindUserByUsername3() {
    String username = "test.ee";
    expectedException.expect(UsernameNotFoundException.class);
    expectedException.expectMessage(containsString(username));
    accountService.loadUserByUsername(username);
  }
}