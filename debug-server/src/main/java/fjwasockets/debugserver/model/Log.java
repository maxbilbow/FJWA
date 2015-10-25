package fjwasockets.debugserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by bilbowm on 23/10/2015.
 */
@Entity
public class Log {

    @Id
    @GeneratedValue
    private Long id;

    private LogType logType;

    private String channel;

    private String message;

    private Date timeStamp;


    public Log()
    {
        timeStamp = new Date();
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.setChannel(logType.channel);
        this.logType = logType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


}
