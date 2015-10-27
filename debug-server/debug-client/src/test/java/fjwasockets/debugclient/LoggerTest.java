package fjwasockets.debugclient;

import org.junit.*;

/**
 * Created by bilbowm on 27/10/2015.
 */
public class LoggerTest {

    private Logger logger;
    private static AnObject log, warning, error;
    int testNumber = 0;

    @BeforeClass
    public static void setUpBeforeClass()
    {
        log = new AnObject("LogObject #1", "THIS IS A LOG");
        warning = new AnObject("LogObject #2", "THIS IS A WARNING");
        error = new AnObject("LogObject #3", "THIS IS AN ERROR");
    }

    @Before
    public void setUp()
    {
        logger = new Logger("Test #" + ++testNumber);
    }

    @After
    public void tearDown()
    {
        logger = null;
        try {
            Thread.sleep(1000); //Optional - useful when checking subscribing clients
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }

    }

    static class AnObject {
        private final String id;
        private final String message;

        AnObject(String id, String message) {
            this.id = id;
            this.message = message;
        }

        public String getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }
    }



    @Test
    public void testSendString() throws Exception {
        logger.send("This is a log",null, "debug.log");
        logger.send("This is a warning",null, "debug.warning");
        logger.send("This is a error",null, "debug.error");
    }

    @Test
    public void testSendObject() throws Exception {
        logger.send(log, null, "debug.log");
        logger.send(warning, null, "debug.warning");
        logger.send(error, null, "debug.error");
    }

    @Test
    @Ignore
    public void testSendStringWithProperties() throws Exception {

    }

    @Test
    @Ignore
    public void testSendObjectWithProperties() throws Exception {

    }
    @Test
    public void testLogWarningString() throws Exception {
        logger.logWarning("This is a warning string");
    }

    @Test
    public void testLogExceptionString() throws Exception {
        logger.logWarning("This is a exception string");
    }

    @Test
    public void testLogMessageString() throws Exception {
        logger.logWarning("This is a log string");
    }

    @Test
    public void testLogWarningObject() throws Exception {
        logger.logWarning(warning);
    }

    @Test
    public void testLogExceptionObject() throws Exception {
        logger.logWarning(error);
    }

    @Test
    public void testLogMessageObject() throws Exception {
        logger.logWarning(log);
    }
}