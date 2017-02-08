package notificationSystem;

import javax.inject.Provider;

import play.api.libs.mailer.SMTPConfiguration;

public class CustomSMTPConfigurationProvider implements Provider<SMTPConfiguration> {

    @Override
    public SMTPConfiguration get() {
        return new SMTPConfiguration("pse.org", 1234, false, false, null, null, false, null, null, false);
    }
}
