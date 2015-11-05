package fjwasockets.debugclient;

/**
 * Created by bilbowm on 05/11/2015.
 */
public class WSLogger implements Logger {

    private final String appId;
    private String endpoint = "ws://localhost:8080/debug-server/listener";

    public WSLogger(String appId)
    {
        this.appId = appId;
    }


    @Override
    public void send(String message, String... routing) throws Exception {

    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public void send(Object object, String... routing) throws Exception {

    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
