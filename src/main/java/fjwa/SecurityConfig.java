package fjwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Created by bilbowm on 29/09/2015.
 */
@Configuration
@EnableWebSecurity
//@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }

    @Override //TODO should not be overridden?
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http //TODO:   <http auto-config="true" use-expressions="true" > (might not be needed)

                .authorizeRequests()
                    .antMatchers("/login.html", "/login.html?error=bad_credentials", "/loginFailed.html", "/logout.html", "/403.html").permitAll()
                    .antMatchers("/admin/**", "/goAway.html").hasRole("ADMIN")
                .antMatchers("/bad.html").not().hasRole("BAD")
                .anyRequest().authenticated()


//                    .accessDecisionManager(accessDecisionManager())
                    .and()
                .formLogin()
                    .loginPage("/login.html").failureUrl("/login.html?error=bad_credentials").permitAll() //("/loginFailed.html").permitAll()
                    .defaultSuccessUrl("/", false)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .and()
                .logout()
                    .logoutUrl("/logout.html").permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout.html", "GET"))
//                    .deleteCookies()
                    .and()
//                .rememberMe()
//                .tokenValiditySeconds(1209600)
//                    .and()
                .exceptionHandling()//.accessDeniedHandler(new MyAccessDeniedHandler())
                .accessDeniedPage("/403.html");//.and().csrf().disable();



    }


//    @Bean
//    public DefaultWebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator()
//    {
//        DefaultWebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator =
//                new DefaultWebInvocationPrivilegeEvaluator(filterSecurityInterceptor());
//        return webInvocationPrivilegeEvaluator;
//    }
//
//    private FilterSecurityInterceptor filterSecurityInterceptor() {
//        return null;
//    }


//    @Autowired
//    private JdbcDaoImpl jdbcDaol;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth
//                .ldapAuthentication()
//                        .groupSearchFilter("member={0}").groupSearchBase("ou=groups")
//                .userSearchBase("ou=people").userSearchFilter("uid={0}")
//                // enable in memory based authentication with a user named

                //DB Usage
              .jdbcAuthentication()
                    .dataSource(dataSource)//.authoritiesByUsernameQuery()
                .passwordEncoder(new BCryptPasswordEncoder());
                //Optional Defauly setup
//                    .withDefaultSchema()
//                    .withUser("user").password("password").roles("USER").and()
//                    .withUser("admin").password("password").roles("USER", "ADMIN");

                //In Memory usage
//                .inMemoryAuthentication()
//                    .withUser("user").password("password").roles("USER").and()
//                    .withUser("admin").password("secret").roles("USER", "ADMIN");

    }

    @Bean //For use with auth.inMemoryAuthentication()
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    // Expose the UserDetailsService as a Bean
//    @Bean
//    @Override
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        return super.userDetailsServiceBean();
//    }







//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        return new AuthenticationProvider() {
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                System.err.println(authentication.getCredentials());
//                return authentication;
//            }
//
//            @Override
//            public boolean supports(Class<?> authentication) {
//                System.err.println("AuthenticationProvider::supports(Class<?> authentication)");
//                return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
//            }
//        };
//    }

}
