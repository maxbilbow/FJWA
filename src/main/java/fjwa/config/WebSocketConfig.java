package fjwa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig {//extends AbstractWebSocketMessageBrokerConfigurer {

//    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
//
////    @Override TODO: THis is the problem...
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/hello").withSockJS();
//    }



}