package notificationSystem;

/**
 * Singleton zum Einstellen der SMTP-Einstellungen.
 */
public class SMTPOptions {

    private String             host              = "PSE";
    private int                port              = 25;
    private boolean            ssl               = false;
    private boolean            tsl               = false;
    private boolean            debug             = false;
    private int                timeout           = 60;
    private int                connectionTimeout = 60;
    private boolean            mock              = false;

    private static SMTPOptions instance;

    private SMTPOptions() {
        // Singleton
    }

    public static SMTPOptions getInstance() {
        if (null == instance) {
            instance = new SMTPOptions();
        }
        return instance;
    }

    /**
     * Setter für die SMTP-Einstellung 'host'.
     * 
     * @param newHost
     *            Der neue host.
     */
    public void changeHostTo(String newHost) {
        host = newHost;
    }

    /**
     * Setter für die SMTP-Einstellung 'port'.
     * 
     * @param newPort
     *            Der neue Port.
     */
    public void changePortTo(int newPort) {
        port = newPort;
    }

    /**
     * Setter für die SMTP-Einstellung 'ssl'.
     * 
     * @param newSsl
     *            Die neue ssl-Einstellung.
     */
    public void changeSslTo(boolean newSsl) {
        ssl = newSsl;
    }

    /**
     * Setter für die SMTP-Einstellung 'tsl'.
     * 
     * @param newTsl
     *            Die neue tsl-Einstellung.
     */
    public void changeTslTo(boolean newTsl) {
        tsl = newTsl;
    }

    /**
     * Setter für die SMTP-Einstellung 'debug'.
     * 
     * @param newDebug
     *            Die neue debug-Einstellung.
     */
    public void changeDebugTo(boolean newDebug) {
        debug = newDebug;
    }

    /**
     * Setter für die SMTP-Einstellung 'mock'.
     * 
     * @param newMock
     *            Die neue mock-Einstellung.
     */
    public void changeMockTo(boolean newMock) {
        mock = newMock;
    }

    /**
     * Setter für die SMTP-Einstellung 'timeout'.
     * 
     * @param newTimeout
     *            Die neue timeout-Einstellung.
     */
    public void changeTimeoutTo(int newTimeout) {
        timeout = newTimeout;
    }

    /**
     * Setter für die SMTP-Einstellung 'connectionTimeout'.
     * 
     * @param newTimeout
     *            Die neue connectionTimeout-Einstellung.
     */
    public void changeConnectionTimeoutTo(int newConnectionTimeout) {
        connectionTimeout = newConnectionTimeout;
    }

    /**
     * Getter für die SMTP-Einstellung 'host'.
     * 
     * @return SMTP-Einstellung 'host'.
     */
    public String getHost() {
        return host;
    }

    /**
     * Getter für die SMTP-Einstellung 'port'.
     * 
     * @return SMTP-Einstellung 'port'.
     */
    public int getPort() {
        return port;
    }

    /**
     * Getter für die SMTP-Einstellung 'ssl'.
     * 
     * @return SMTP-Einstellung 'ssl'.
     */
    public boolean getSsl() {
        return ssl;
    }

    /**
     * Getter für die SMTP-Einstellung 'tsl'.
     * 
     * @return SMTP-Einstellung 'tsl'.
     */
    public boolean getTsl() {
        return tsl;
    }

    /**
     * Getter für die SMTP-Einstellung 'debug'.
     * 
     * @return SMTP-Einstellung 'debug'.
     */
    public boolean getDebug() {
        return debug;
    }

    /**
     * Getter für die SMTP-Einstellung 'mock'.
     * 
     * @return SMTP-Einstellung 'mock'.
     */
    public boolean getMock() {
        return mock;
    }

    /**
     * Getter für die SMTP-Einstellung 'timeout'.
     * 
     * @return SMTP-Einstellung 'timeout'.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Getter für die SMTP-Einstellung 'connectionTimeout'.
     * 
     * @return SMTP-Einstellung 'connectionTimeout'.
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
}
