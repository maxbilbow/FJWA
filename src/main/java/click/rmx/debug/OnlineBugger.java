package click.rmx.debug;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bilbowm on 13/10/2015.
 */
public class OnlineBugger {


    private static OnlineBugger instance;

    public static OnlineBugger getInstance()
    {
        return instance != null ? instance : new OnlineBugger();
    }

    private OnlineBugger()
    {
        instance = this;
    }

    private final Set<String> errors = new HashSet<>();

    public String getErrorHtml() {
            String log = "<ul>";// + this.errorLog;
            for (String error : errors) {
                log += "<li>" + error.replace("\n","<br/>") + "</li>";
            }
            return log + "</ul>";//.replace("\n", "<br/>");
    }

    public void addHtml(String html) {
        this.errors.add(html);
    }
}
