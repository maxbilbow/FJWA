package click.rmx.threads;

import java.util.HashMap;

import static click.rmx.threads.RMXThreadGroup.*;
import static click.rmx.threads.RMXThreadGroup.Exceptional;

/**
 * Created by bilbowm on 14/10/2015.
 */
public class RMXThreadMap<T> extends HashMap<T, Thread> {


    public RMXThreadMap()
    {
        super();
    }

    public RMXThreadMap(Exceptional exceptionHandler, ThreadCompletionHandler completionHandler)
    {
        super();
        this.defaultCompletionHandler = completionHandler;
        this.defaultFailAction = exceptionHandler;
    }
    private Exceptional defaultFailAction = null;

    private ThreadCompletionHandler defaultCompletionHandler = null;

    public Thread runOnAfterThread(Runnable runnable, T threadId) {
        return runOnAfterThread(runnable, null, threadId);
    }

    public Thread runOnAfterThread(Runnable runnable, Exceptional failAction, T threadId) {
        Thread thread = this.get(threadId);
        if (thread != null && thread.isAlive())
            try {
                thread.join();
            } catch (InterruptedException e) {
                if (failAction != null)
                    failAction.run(e);
                else {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        this.put(threadId, thread = runOnNewThread(runnable, failAction));
        return thread;
    }

    public Thread runOnNewThread(Runnable runnable, Exceptional failAction)
    {
        return RMXThreadGroup.runOnNewThread(runnable,failAction, defaultCompletionHandler);
    }

    public Thread runOnNewThread(Runnable runnable, Exceptional failAction, ThreadCompletionHandler completionHandler)
    {
        return RMXThreadGroup.runOnNewThread(runnable,failAction, completionHandler);
    }

    public Thread runOnNewThread(Runnable runnable)
    {
        return RMXThreadGroup.runOnNewThread(runnable,defaultFailAction, defaultCompletionHandler);
    }

    public boolean hasDefaultFailAction() {
        return defaultFailAction != null;
    }

    public void setDefaultFailAction(Exceptional defaultFailAction) {
        this.defaultFailAction = defaultFailAction;
    }

    public void setDefaultCompletionHandler(ThreadCompletionHandler defaultCompletionHandler) {
        this.defaultCompletionHandler = defaultCompletionHandler;
    }
}
