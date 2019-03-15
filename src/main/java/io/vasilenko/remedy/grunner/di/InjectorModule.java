package io.vasilenko.remedy.grunner.di;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.pluginsvr.ARPluginServerConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

@Slf4j
public class InjectorModule extends AbstractModule {

    @Provides
    @Singleton
    GroovyShell provideGroovyShell(Binding binding) {
        GroovyShell groovyShell = new GroovyShell(binding);
        log.debug("GroovyShell created");
        return groovyShell;
    }

    @Provides
    @Singleton
    Binding provideBinding(ARServerUser arServerUser) {
        Binding binding = new Binding();
        binding.setVariable("ars", arServerUser);
        log.debug("Binding created");
        return binding;
    }

    @Provides
    @Singleton
    ARServerUser provideARServerUser(Configuration configuration) {
        ARServerUser arServerUser = null;
        try {
            arServerUser = ARPluginServerConfiguration.getInstance().getARSvrUsr();
            log.debug("ARServerUser created");
        } catch (ARException e) {
            log.error(e.getMessage(), e);
        }
        return arServerUser;
    }

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
