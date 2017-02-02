package startup;

import com.google.inject.AbstractModule;

public class StartupModule extends AbstractModule {

    protected void configure() {
        System.out.println("OnStartupConfiguration");
        bind(StartupInterface.class).to(StartupCode.class).asEagerSingleton();
    }
}
