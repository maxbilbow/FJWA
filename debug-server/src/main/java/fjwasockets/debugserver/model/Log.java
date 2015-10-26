package fjwasockets.debugserver.model;

import click.rmx.debug.Bugger;
import click.rmx.debug.WebBugger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
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

    @Transient
    private String html = null;

    private String shortTime;

    public Log()
    {
        timeStamp = new Date();
        shortTime = Bugger.timestamp();
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
        this.logType = logType;
        this.channel = logType.channel;
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


    public String getHtml() {
        return html == null? WebBugger.toHtml(message) : html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getShortTime() {
        return shortTime != null ? shortTime : timeStamp.toString();
    }

    public void setShortTime(String shortTime) {
        this.shortTime = shortTime;
    }
}
