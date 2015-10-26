package fjwa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

/**
 * Created by bilbowm on 22/10/2015.
 */
@Configuration
//@EnableWebSocketMessageBroker
//@ComponentScan(basePackages = "fjwa.controller")
public class WebSocketConfig {//extends AbstractWebSocketMessageBrokerConfigurer {

//    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat").withSockJS();
//    }
}