package fjwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

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
                    .antMatchers("/login.html", "/login.html?error=bad_credentials", "/loginFailed.html", "/logout.html", "/403.html").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").hasRole("USER")//hasRole('USER')")// and hasRole('DBA')")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login.html").failureUrl("/login.html?error=bad_credentials").permitAll() //("/loginFailed.html").permitAll()
                    .defaultSuccessUrl("/boom.html", false)
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .and()
                .logout()
                    .logoutUrl("/logout.html").permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout.html", "GET"))
                    .and()
//                .rememberMe()
//                    .tokenValiditySeconds(1209600)
//                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/403.html")
                    .and()
                .csrf().disable();



    }

//    <authentication-manager>
//    <ldap-authentication-provider
//    group-search-filter="member={0}"
//    group-search-base="ou=groups"
//    user-search-base="ou=people"
//    user-search-filter="uid={0}" />
//    </authentication-manager>
    @Autowired
    private DataSource dataSource;

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth
//                .ldapAuthentication()
//                        .groupSearchFilter("member={0}").groupSearchBase("ou=groups")
//                .userSearchBase("ou=people").userSearchFilter("uid={0}")
//                // enable in memory based authentication with a user named
//                .and()
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .withDefaultSchema()
////                .withUser("user").password("password").roles("USER").and()
////                .withUser("admin").password("password").roles("USER", "ADMIN")
//                .and()
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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//Data
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "fjwa.model" });

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return hibernateProperties;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/fjwa?autoReconnect=true&createDatabaseIfNotExist=true");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }



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
