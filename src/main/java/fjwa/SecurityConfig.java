package fjwa;

import click.rmx.debug.WebBugger;
import fjwa.security.FitnessPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebBugger debug;

//    @Autowired
//    PermissionEvaluator permissionEvaluator;



    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler permissionHandler = new DefaultMethodSecurityExpressionHandler();
        permissionHandler.setPermissionEvaluator(new FitnessPermissionEvaluator(dataSource,debug));
        return permissionHandler;
    }

//    @Bean
//    public PermissionEvaluator permissionEvaluator()
//    {
//        return new FitnessPermissionEvaluator(dataSource,debug);
//    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http

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
                .rememberMe()
                .tokenValiditySeconds(1209600)
                    .and()
                .exceptionHandling()//.accessDeniedHandler(new MyAccessDeniedHandler())
                .accessDeniedPage("/403.html");//.and().csrf().disable();



    }



//    @Autowired
//    private JdbcDaoImpl jdbcDaol;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //DB Usage
              .jdbcAuthentication()
                    .dataSource(dataSource)//.authoritiesByUsernameQuery()
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean //For use with auth.inMemoryAuthentication()
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
