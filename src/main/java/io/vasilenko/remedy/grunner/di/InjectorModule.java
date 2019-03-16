/*
 * Copyright 2019 Sergey Vasilenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vasilenko.remedy.grunner.di;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.ARPluginServerConfiguration;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.vasilenko.remedy.grunner.service.GrunnerService;
import io.vasilenko.remedy.grunner.service.impl.FileScriptService;
import io.vasilenko.remedy.grunner.service.impl.InlineScriptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import static com.google.inject.Scopes.SINGLETON;

@Slf4j
public class InjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        MapBinder<Value, GrunnerService> grunnerServiceBinder = MapBinder.newMapBinder(binder(), Value.class, GrunnerService.class);
        grunnerServiceBinder.addBinding(new Value("FILE"))
                .to(FileScriptService.class)
                .in(SINGLETON);
        grunnerServiceBinder.addBinding(new Value("INLINE"))
                .to(InlineScriptService.class)
                .in(SINGLETON);
    }

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
    ARServerUser provideARServerUser() {
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
