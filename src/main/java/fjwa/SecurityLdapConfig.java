package fjwa;

import click.rmx.debug.WebBugger;
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
//@ImportResource("users.ldif")
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityLdapConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebBugger debug;

//    @Bean
//    public DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
//        DefaultMethodSecurityExpressionHandler permissionHandler = new DefaultMethodSecurityExpressionHandler();
//        permissionHandler.setPermissionEvaluator(new FitnessPermissionEvaluator(dataSource, debug));
//        return permissionHandler;
//    }

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


//    @Autowired
//    private JdbcDaoImpl jdbcDaol;

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String url = getClass().getResource("users.ldif").getFile();
        auth
                .ldapAuthentication()
                .groupSearchFilter("member={0}")
                    .groupSearchBase("ou=groups")
                .userSearchBase("ou=people")
                    .userSearchFilter("uid={0}")
        .contextSource().url((url));



    }


    @Bean //For use with auth.inMemoryAuthentication()
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public LdapContext contextSource() throws NamingException {
//        InitialLdapContext context = new InitialLdapContext();
////        context.
//        return context;
//    }


//    @Bean
//    public LdapServerBeanDefinitionParser ldapServer()
//    {
//        LdapServerBeanDefinitionParser ldapServer = new LdapServerBeanDefinitionParser();
////        ldapServer.parse()
//        return ldapServer;
//    }

//    public MethodSecurityMetadataSourceAdvisor methodSecurityMetadataSourceAdvisor()
//    {
//        Metada
//    }
}