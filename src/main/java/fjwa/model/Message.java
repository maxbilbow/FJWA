package fjwa.model;

import javax.validation.constraints.NotNull;

/**
 * Created by bilbowm on 20/10/2015.
 */
public class Message {

    @NotNull
    private String message = "";

    @NotNull
    private String topic = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
