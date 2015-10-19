package rabbit;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rabbit.tut2.NewTask;

/**
 * Created by bilbowm on 19/10/2015.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
        for (String msg :args) {
            System.out.println(msg);
            if (msg.contains("-Rabbit:send")) {
                NewTask.send(args);
            }
            if (msg.contains("-Rabbit:receive")) {
                NewTask.receive(args);
            }
        }

    }
}
