package fjwa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by bilbowm on 16/10/2015.
 */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityInMemConfig extends WebSecurityConfigurerAdapter {



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

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("secret").roles("USER", "ADMIN");

    }

    @Bean //For use with auth.inMemoryAuthentication()
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}