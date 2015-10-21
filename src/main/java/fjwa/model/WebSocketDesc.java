package fjwa.model;

/**
 * Created by bilbowm on 20/10/2015.
 */
public class WebSocketDesc {
    private final String uri;

    private boolean secure = false;

    public WebSocketDesc(String uri) {
        if (uri.startsWith("ws://"))
            uri = uri.substring(5);
        if (uri.startsWith("wss://"))
            uri = uri.substring(6);
        if (uri.endsWith("/"))
            this.uri = uri.substring(0,uri.length() - 1);
        else
            this.uri = uri;
    }

    public String getUri() {
        return (secure ? "wss://" : "ws://") + uri;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
