package notificationSystem;

import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import play.api.libs.mailer.SMTPConfiguration;
import scala.collection.Seq;

public class CustomMailerConfigurationModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(Environment arg0, Configuration arg1) {
        return seq(bind(SMTPConfiguration.class).toProvider(CustomSMTPConfigurationProvider.class));
    }

}