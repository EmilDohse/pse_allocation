package notificationSystem;

import javax.inject.Provider;

import play.api.libs.mailer.SMTPConfiguration;

/**
 * 
 *
 */
public class CustomSMTPConfigurationProvider implements Provider<SMTPConfiguration> {

    /**
     * 
     */
    @Override
    public SMTPConfiguration get() {
        return new SMTPConfiguration(SMTPOptions.getHost(), SMTPOptions.getPort(), SMTPOptions.getSsl(),
                SMTPOptions.getTsl(), null, null, SMTPOptions.getDebug(), null, null, SMTPOptions.getMock());
    }
}
