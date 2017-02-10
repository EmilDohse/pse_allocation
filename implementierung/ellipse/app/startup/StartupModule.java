package startup;

import com.google.inject.AbstractModule;

public class StartupModule extends AbstractModule {

    protected void configure() {
        bind(StartupInterface.class).to(StartupCode.class).asEagerSingleton();
    }
}
