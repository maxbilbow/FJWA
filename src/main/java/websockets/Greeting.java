package websockets;

/**
 * Created by bilbowm on 21/10/2015.
 */
public class Greeting {

    private String content;

    public Greeting(String content)
    {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
