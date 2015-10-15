package click.rmx.core;

import click.rmx.debug.OnlineBugger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by bilbowm on 15/10/2015.
 */
@Configuration
public class RMXSpringConfig
{
    @Bean
    public OnlineBugger debug()
    {
        return OnlineBugger.getInstance();
    }
}
