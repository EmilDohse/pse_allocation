package notificationSystem;

/*** Singleton zum Einstellen der SMTP-Einstellungen. */
public class SMTPOptions {

    private String             host;
    private String             mailFrom;
    private int                port;
    private boolean            ssl;
    private boolean            tsl;
    private boolean            debug;
    private int                timeout;
    private int                connectionTimeout;
    private String             username;
    private String             password;

    private static SMTPOptions instance;

    private SMTPOptions() {
        host = "smtp.kit.edu";
        mailFrom = "noreply@kit.edu";
        port = 25;
        ssl = false;
        tsl = false;
        debug = false;
        timeout = 60;
        connectionTimeout = 60;
        username = "admin";
        password = "admin";
    }

    public static SMTPOptions getInstance() {
        if (null == instance) {
            instance = new SMTPOptions();
        }
        return instance;
    }

    /**
     * Getter für den Absender der Mails.
     * 
     * @return
     */
    public String getMailFrom() {
        return mailFrom;
    }

    /**
     * Setter für den Absender der Mails.
     *
     * @param newHost
     *            Der neue host.
     */
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    /**
     * Getter für den Benutzernamen für die Authentifizierung beim SMTP-Server.
     *
     * @return SMTP-Einstellung 'debug'.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter für den Benutzernamen für die Authentifizierung beim SMTP-Server.
     *
     * @param newHost
     *            Der neue host.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter für das Passwort für die Authentifizierung beim SMTP-Server.
     *
     * @return SMTP-Einstellung 'debug'.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter für das Passwort für die Authentifizierung beim SMTP-Server.
     *
     * @param newHost
     *            Der neue host.
     */
    public void setPassword(String password) {
        this.password = password;
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
    public boolean getTls() {
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
