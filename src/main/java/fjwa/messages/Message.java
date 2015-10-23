package fjwa.messages;

/**
 * Created by bilbowm on 20/10/2015.
 */
public class Message {//} implements WebSocketMessage<String> {

    private String message, topic;
    private int id;

    public Message() {

    }

    public Message(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

//    @Override
    public String getPayload() {
        return getMessage();
    }

//    @Override
    public int getPayloadLength() {
        return message.length();
    }

//    @Override
    public boolean isLast() {
        return true;
    }
}