package data;

import java.util.NoSuchElementException;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;

/*** Singleton zum Einstellen der SMTP-Einstellungen. */
@Entity
public class SMTPOptions extends ElipseModel {

    private static final String KEY_FILE = "conf/keyset";

    @Transient
    private Crypter             crypter;

    @NotNull
    private String             host;
    @NotNull
    @Pattern(regexp = ".+@.+")
    private String             mailFrom;
    @Min(1)
    private int                port;
    private boolean            ssl;
    private boolean            tls;
    private boolean            debug;
    private int                timeout;
    private int                connectionTimeout;
    @NotNull
    private String             username;
    @NotNull
    private String             password;

    private static SMTPOptions instance;

    /**
     * @deprecated !!!DO NOT USE THIS!!! SMTPOptions is supposed to be a
     *             Singleton. Constructor is only public due to restrictions in
     *             EBean. Use GeneralData.getInstance() instead.
     */
    @Deprecated
    public SMTPOptions() {
        try {
            crypter = new Crypter(KEY_FILE);
            password = crypter.encrypt("admin");
        } catch (KeyczarException e) {
            // Kann eigentlich nicht auftreten
            crypter = null;
            password = "error";
            e.printStackTrace();
            assert false;
        }
        host = "smtp.kit.edu";
        mailFrom = "noreply@kit.edu";
        port = 25;
        ssl = false;
        tls = false;
        debug = false;
        timeout = 60;
        connectionTimeout = 60;
        username = "admin";
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
            return crypter.decrypt(password);
        } catch (KeyczarException e) {
            // Kann eigentlich nicht auftreten
            e.printStackTrace();
            assert false;
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
            setPassword(crypter.encrypt(plainTextPassword));
        } catch (KeyczarException e) {
            // Kann eigentlich nicht auftreten
            e.printStackTrace();
            assert false;
        }
    }

    /**
     * Setter für die SMTP-Einstellung 'host'.
     *
     * @param newHost
     *            Der neue host.
     */
    public void setHost(String newHost) {
        host = newHost;
    }

    /**
     * Setter für die SMTP-Einstellung 'port'.
     *
     * @param newPort
     *            Der neue Port.
     */
    public void setPort(int newPort) {
        port = newPort;
    }

    /**
     * Setter für die SMTP-Einstellung 'ssl'.
     *
     * @param newSsl
     *            Die neue ssl-Einstellung.
     */
    public void setSsl(boolean newSsl) {
        ssl = newSsl;
    }

    /**
     * Setter für die SMTP-Einstellung 'tls'.
     *
     * @param newTls
     *            Die neue tls-Einstellung.
     */
    public void setTls(boolean newTls) {
        tls = newTls;
    }

    /**
     * Setter für die SMTP-Einstellung 'debug'.
     *
     * @param newDebug
     *            Die neue debug-Einstellung.
     */
    public void setDebug(boolean newDebug) {
        debug = newDebug;
    }

    /**
     * Setter für die SMTP-Einstellung 'timeout'.
     *
     * @param newTimeout
     *            Die neue timeout-Einstellung.
     */
    public void setTimeout(int newTimeout) {
        timeout = newTimeout;
    }

    /**
     * Setter für die SMTP-Einstellung 'connectionTimeout'.
     *
     * @param newTimeout
     *            Die neue connectionTimeout-Einstellung.
     */
    public void setConnectionTimeout(int newConnectionTimeout) {
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
     * Getter für die SMTP-Einstellung 'tls'.
     *
     * @return SMTP-Einstellung 'tls'.
     */
    public boolean getTls() {
        return tls;
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
