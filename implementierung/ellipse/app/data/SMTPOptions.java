package data;

import java.util.NoSuchElementException;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;

/*** Singleton zum Einstellen der SMTP-Einstellungen. */
@Entity
public class SMTPOptions extends ElipseModel {

    private static final String KEY_FILE = "conf/keyset";

    @NotNull
    private String             host;
    @NotNull
    @Pattern(regexp = ".+@.+")
    private String             mailFrom;
    @Min(1)
    private int                port;
    private boolean            ssl;
    private boolean            tsl;
    private boolean            debug;
    private int                timeout;
    private int                connectionTimeout;
    @NotNull
    private String             username;
    @NotNull
    private String             password;

    private static SMTPOptions instance;

    /**
     * !!!DO NOT USE THIS!!! GeneralData is supposed to be a Singleton.
     * Constructor is only public due to restrictions in EBean. Use
     * GeneralData.getInstance() instead.
     */
    @Deprecated
    public SMTPOptions() {
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

    /**
     * Gibt die SMTOptions Instanz zurück. Läd sie gegebenenfalls aus der
     * Datenbank. Falls keine Instanz in der Datenbank ist, wird eine neue
     * eingefügt.
     * 
     * @return Die Instanz
     */
    public static SMTPOptions getInstance() {
        if (null == instance) {
            try {
                instance = SMTPOptions.getAll(SMTPOptions.class).stream()
                        .findFirst().get();
            } catch (NoSuchElementException e) {
                instance = new SMTPOptions();
                instance.save();
            }
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
     * @return Das entschlüsselte Passwort.
     */
    public String getPassword() {
        // Entschlüssele Passwort
        try {
            Crypter crypter = new Crypter(KEY_FILE);
            return crypter.decrypt(password);
        } catch (KeyczarException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Setter für das Passwort für die Authentifizierung beim SMTP-Server.
     *
     * @param encryptedPassword
     *            Das verschlüsselte neue Passwort.
     */
    public void setPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
    
    /**
     * Verschlüsselt das übergebene Passwort und setzt es als Attribut.
     * 
     * @param plainTextPassword
     *            Das unverschlüsselte Passwort.
     */
    public void savePassword(String plainTextPassword) {
        // Verschlüssel Passwort
        try {
            Crypter crypter = new Crypter(KEY_FILE);
            setPassword(crypter.encrypt(plainTextPassword));
        } catch (KeyczarException e) {
            e.printStackTrace();
        }
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
