package fjwa.model;

/**
 * Created by bilbowm on 20/10/2015.
 */
public class WebSocketDesc {
    private final String uri;

    public WebSocketDesc(String uri) {
        if (uri.startsWith("ws://"))
            uri = uri.substring(5);
        if (uri.startsWith("wss://"))
            uri = uri.substring(6);
        if (uri.endsWith("/"))
            this.uri = uri;
        else
            this.uri = uri + "/";
    }

    public String getWsUri() {
        return "ws://" + uri;
    }

    public String getWssUri() {
        return "wss://"+uri;
    }

    public String getUri() {
        return uri;
    }
}
