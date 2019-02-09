package io.vasilenko.remedy.grunner.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

@Slf4j
public class InjectorModule extends AbstractModule {

    @Provides
    @Singleton
    Configuration provideConfiguration() {
        Configuration configuration = null;
        try {
            configuration = new PropertiesConfiguration("plugin.properties");
        } catch (ConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        log.debug("Configuration created");
        return configuration;
    }
}
