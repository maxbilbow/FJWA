package fjwa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Created by bilbowm on 15/10/2015.
 */
@Configuration
public class DBConfig implements TransactionManagementConfigurer {

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager(entityManagerFactory);
    }

    enum DB {
        LOCAL("jdbc:mysql://localhost:3306/fjwa",
                "root", "password"),
        MAX_BILBOW("jdbc:mysql://devsql.maxbilbow.com/spring_mvc_dev",
                "maxbilbow", "Purple22"),
        AMAZON("jdbc:mysql://rmxdb-test.c8kzyhurz6of.us-west-2.rds.amazonaws.com:3306/fjwa",
                "root", "password");

        public final String url, username, password;

        DB(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }
    }
    private final DB db = DB.LOCAL;

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
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");// USE_REMOTE_DATABASE ? "update" : "create-drop");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return hibernateProperties;
    }


    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(db.url + "?autoReconnect=true&createDatabaseIfNotExist=true");
        dataSource.setUsername(db.username);
        dataSource.setPassword(db.password);
        return dataSource;
    }

    @Autowired
    private EntityManagerFactory entityManagerFactory;

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

}
