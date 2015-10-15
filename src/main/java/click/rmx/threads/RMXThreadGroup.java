package click.rmx.threads;

import java.time.Duration;
import java.time.Instant;

/**
 * Created by bilbowm on 14/10/2015.
 */
public class RMXThreadGroup extends ThreadGroup {

    private RMXThreadGroup() {
        super("RMXThreadGroup");
    }

    public static class RMXThread extends Thread {

        public RMXThread(ThreadGroup threadGroup, Runnable runnable, String name) {
            super(threadGroup, runnable, name);
        }
    }
    private static RMXThreadGroup threadGroup;// = new ThreadGroup("RMXThreads");
    public static RMXThread newThread(Runnable runnable)
    {
        RMXThread thread = new RMXThread(defaultGroup() ,runnable, "RMXThread");
        return thread;
    }
    public static RMXThread runOnNewThread(Runnable runnable) {
        return runOnNewThread(runnable, null, null);
    }

    public static RMXThread runOnNewThread(Runnable runnable, ThreadCompletionHandler completionHandler) {
        return runOnNewThread(runnable, null, completionHandler);
    }

    public static RMXThread runOnNewThread(Runnable runnable, Exceptional exceptionHandler) {
        return runOnNewThread(runnable, exceptionHandler, null);
    }

    public static RMXThread runOnNewThread(Runnable runnable, Exceptional failAction, ThreadCompletionHandler completionHandler) {
        RMXThread thread = thread = newThread(() -> {
            ThreadData threadData = new ThreadData();
            boolean success = false;
            Instant start = Instant.now();
            try {
                runnable.run();
                success = true;
            } catch (Exception e) {
                if (failAction != null)
                    failAction.run(e);
                else {
                    e.printStackTrace();
                    System.exit(1);
                }
            } finally {
                if (completionHandler != null) {

                    completionHandler.run(threadData.end(success));
                }
            }
        });

        thread.start();//.run();
        return thread;
    }

    public static RMXThreadGroup defaultGroup() {
        return threadGroup != null ? threadGroup : (threadGroup = new RMXThreadGroup());
    }

    public interface Exceptional {
        void run(Exception e);
    }

    public static class ThreadData {
        public final Thread thread;
        private Duration duration;
        public final Instant startTime;
        private boolean success;

        public ThreadData()
        {
            thread = Thread.currentThread();
            startTime = Instant.now();
        }
        public Duration getDuration() {
            return duration;
        }

        public ThreadData end(boolean success)
        {
            this.success = success;
            duration = Duration.between(startTime,Instant.now());
            return this;
        }

        public boolean isSuccess() {
            return success;
        }
    }
    public interface ThreadCompletionHandler {
        void run(ThreadData threadData);
    }

}