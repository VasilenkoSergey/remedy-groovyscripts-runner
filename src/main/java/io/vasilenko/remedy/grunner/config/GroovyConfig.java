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

package io.vasilenko.remedy.grunner.config;

import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GroovyConfig {

    @Value("${grunner.scripts.path}")
    private String scriptsPath;

    @Bean
    GroovyScriptEngine groovyScriptEngine() throws IOException {
        return new GroovyScriptEngine(scriptsPath);
    }

    @Bean
    GroovyShell groovyShell(GroovyScriptEngine groovyScriptEngine) {
        return new GroovyShell(groovyScriptEngine.getGroovyClassLoader());
    }
}
