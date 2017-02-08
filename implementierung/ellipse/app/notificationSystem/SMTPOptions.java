package notificationSystem;

/**
 * Singleton zum Einstellen der SMTP-Einstellungen.
 */
public class SMTPOptions {

    private static String  host              = "PSE";
    private static int     port              = 25;
    private static boolean ssl               = false;
    private static boolean tsl               = false;
    private static boolean debug             = false;
    private static int     timeout           = 60;
    private static int     connectionTimeout = 60;
    private static boolean mock              = false;

    /**
     * Setter für die SMTP-Einstellung 'host'.
     * 
     * @param newHost
     *            Der neue host.
     */
    public static void changeHostTo(String newHost) {
        host = newHost;
    }

    /**
     * Setter für die SMTP-Einstellung 'port'.
     * 
     * @param newPort
     *            Der neue Port.
     */
    public static void changePortTo(int newPort) {
        port = newPort;
    }

    /**
     * Setter für die SMTP-Einstellung 'ssl'.
     * 
     * @param newSsl
     *            Die neue ssl-Einstellung.
     */
    public static void changeSslTo(boolean newSsl) {
        ssl = newSsl;
    }

    /**
     * Setter für die SMTP-Einstellung 'tsl'.
     * 
     * @param newTsl
     *            Die neue tsl-Einstellung.
     */
    public static void changeTslTo(boolean newTsl) {
        tsl = newTsl;
    }

    /**
     * Setter für die SMTP-Einstellung 'debug'.
     * 
     * @param newDebug
     *            Die neue debug-Einstellung.
     */
    public static void changeDebugTo(boolean newDebug) {
        debug = newDebug;
    }

    /**
     * Setter für die SMTP-Einstellung 'mock'.
     * 
     * @param newMock
     *            Die neue mock-Einstellung.
     */
    public static void changeMockTo(boolean newMock) {
        mock = newMock;
    }

    /**
     * Setter für die SMTP-Einstellung 'timeout'.
     * 
     * @param newTimeout
     *            Die neue timeout-Einstellung.
     */
    public static void changeTimeoutTo(int newTimeout) {
        timeout = newTimeout;
    }

    /**
     * Setter für die SMTP-Einstellung 'connectionTimeout'.
     * 
     * @param newTimeout
     *            Die neue connectionTimeout-Einstellung.
     */
    public static void changeConnectionTimeoutTo(int newConnectionTimeout) {
        connectionTimeout = newConnectionTimeout;
    }

    /**
     * Getter für die SMTP-Einstellung 'host'.
     * 
     * @return SMTP-Einstellung 'host'.
     */
    public static String getHost() {
        return host;
    }

    /**
     * Getter für die SMTP-Einstellung 'port'.
     * 
     * @return SMTP-Einstellung 'port'.
     */
    public static int getPort() {
        return port;
    }

    /**
     * Getter für die SMTP-Einstellung 'ssl'.
     * 
     * @return SMTP-Einstellung 'ssl'.
     */
    public static boolean getSsl() {
        return ssl;
    }

    /**
     * Getter für die SMTP-Einstellung 'tsl'.
     * 
     * @return SMTP-Einstellung 'tsl'.
     */
    public static boolean getTsl() {
        return tsl;
    }

    /**
     * Getter für die SMTP-Einstellung 'debug'.
     * 
     * @return SMTP-Einstellung 'debug'.
     */
    public static boolean getDebug() {
        return debug;
    }

    /**
     * Getter für die SMTP-Einstellung 'mock'.
     * 
     * @return SMTP-Einstellung 'mock'.
     */
    public static boolean getMock() {
        return mock;
    }

    /**
     * Getter für die SMTP-Einstellung 'timeout'.
     * 
     * @return SMTP-Einstellung 'timeout'.
     */
    public static int getTimeout() {
        return timeout;
    }

    /**
     * Getter für die SMTP-Einstellung 'connectionTimeout'.
     * 
     * @return SMTP-Einstellung 'connectionTimeout'.
     */
    public static int getConnectionTimeout() {
        return connectionTimeout;
    }
}
