package notificationSystem;

import javax.inject.Provider;

import play.api.libs.mailer.SMTPConfiguration;

/**
 * 
 *
 */
public class CustomSMTPConfigurationProvider implements Provider<SMTPConfiguration> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SMTPConfiguration get() {
        SMTPOptions options = SMTPOptions.getInstance();
        return new SMTPConfiguration(options.getHost(), options.getPort(),
                options.getSsl(), options.getTsl(), null, null,
                options.getDebug(), null, null, options.getMock());
    }
}
