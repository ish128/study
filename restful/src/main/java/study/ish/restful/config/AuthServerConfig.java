package study.ish.restful.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import study.ish.restful.accounts.AccountService;
import study.ish.restful.common.AppProperties;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private AccountService accountService;

  @Autowired
  private TokenStore tokenStore;

  @Autowired
  private AppProperties appProperties;

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    //client  설정
    clients.inMemory()
        .withClient(appProperties.getClientId())
        .authorizedGrantTypes("password", "refresh_token")
        .scopes("read", "write")
        .secret(passwordEncoder.encode(appProperties.getClientSecret()))
        .accessTokenValiditySeconds(10 * 60)
        .refreshTokenValiditySeconds(6 * 10 * 60);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    //인증 엔트리 포인트 설정
    endpoints.authenticationManager(authenticationManager)
        .userDetailsService(accountService)
        .tokenStore(tokenStore);
  }
}
