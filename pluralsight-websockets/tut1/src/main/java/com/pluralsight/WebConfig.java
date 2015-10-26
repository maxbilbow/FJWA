package com.pluralsight;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by bilbowm on 26/10/2015.
 */
@Configuration
//@EnableWebMvc
@Import(EndpointApplicationConfig.class)
public class WebConfig {
}
