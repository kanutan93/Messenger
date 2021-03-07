package ru.just.messenger.config.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${origin}")
  private String origin;

  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl) {
    this.userDetailsServiceImpl = userDetailsServiceImpl;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, SecurityConst.SIGN_UP_URL)
        .permitAll()
        .antMatchers(HttpMethod.GET, SecurityConst.IS_LOGGED_IN_URL)
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilter(getAuthenticationFilter())
        .addFilter(getAuthorizationFilter())
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  /**
   * Cors filter configuration.
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(origin));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  private JwtAuthenticationFilter getAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager());
    filter.setFilterProcessesUrl(SecurityConst.SIGN_IN_URL);
    return filter;
  }

  private JwtAuthorizationFilter getAuthorizationFilter() throws Exception {
    JwtAuthorizationFilter filter = new JwtAuthorizationFilter(authenticationManager());
    return filter;
  }
}
