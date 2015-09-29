package fjwa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by bilbowm on 29/09/2015.
 */
@Configuration
@EnableWebSecurity
//@EnableAutoConfiguration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override //TODO should not be overridden?
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http //TODO:   <http auto-config="true" use-expressions="true" > (might not be needed)
                .authorizeRequests()
                .antMatchers("/login.html", "loginFailed.html", "logout.html", "403.html").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/**").access("hasRole('USER')") // and hasRole('DBA')
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html").failureUrl("/loginFailed.html")
                .and()
                .logout()
                .logoutUrl("/logout.html")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403.html");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth

                // enable in memory based authentication with a user named
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("secret").roles("USER", "ADMIN");

    }

    // Expose the UserDetailsService as a Bean
    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
}
