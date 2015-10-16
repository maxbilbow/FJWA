package click.rmx.spring;

import click.rmx.debug.OnlineBugger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by bilbowm on 15/10/2015.
 */
@Configuration
public class RMXSpringConfig
{
    @Bean
    @Autowired
    public OnlineBugger debug()
    {
        return OnlineBugger.getInstance();
    }
}
