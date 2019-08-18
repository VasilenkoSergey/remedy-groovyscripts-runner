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

import com.bmc.arsys.api.Value;
import io.vasilenko.remedy.grunner.service.ScriptRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ScriptRunnerConfig {

    @Bean(name = "serviceMap")
    Map<Value, ScriptRunner> serviceMap(
            @Qualifier("fileScriptRunner")ScriptRunner fileScriptRunner,
            @Qualifier("entryScriptRunner")ScriptRunner entryScriptRunner,
            @Qualifier("inlineScriptRunner")ScriptRunner inlineScriptRunner) {

        Map<Value, ScriptRunner> serviceMap = new HashMap<>();
        serviceMap.put(new Value(ScriptType.FILE.name()), fileScriptRunner);
        serviceMap.put(new Value(ScriptType.ENTRY.name()), entryScriptRunner);
        serviceMap.put(new Value(ScriptType.INLINE.name()), inlineScriptRunner);
        return serviceMap;
    }
}
