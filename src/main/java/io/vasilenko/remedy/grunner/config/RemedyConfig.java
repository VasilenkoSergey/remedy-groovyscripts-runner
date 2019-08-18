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

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.pluginsvr.ARPluginServerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RemedyConfig {

    @Value("${grunner.remedy.server:}")
    private String server;
    @Value("${grunner.remedy.port:0}")
    private int port;
    @Value("${grunner.remedy.user:}")
    private String user;
    @Value("${grunner.remedy.pwd:}")
    private String pwd;

    @Bean
    @Profile("local")
    public ARServerUser localARServerUser() {
        ARServerUser arServerUser = new ARServerUser();
        arServerUser.setServer(server);
        arServerUser.setPort(port);
        arServerUser.setUser(user);
        arServerUser.setPassword(pwd);
        return arServerUser;
    }

    @Bean
    @Profile("!local")
    public ARServerUser arServerUser() throws ARException {
        return ARPluginServerConfiguration.getInstance().getARSvrUsr();
    }
}
