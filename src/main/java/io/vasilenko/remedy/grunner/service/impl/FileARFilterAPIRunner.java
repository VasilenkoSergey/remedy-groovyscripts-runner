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

package io.vasilenko.remedy.grunner.service.impl;

import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import groovy.util.GroovyScriptEngine;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.script.GrunnerARFilterAPIPlugin;
import io.vasilenko.remedy.grunner.service.BaseARFilterAPIRunner;
import io.vasilenko.remedy.grunner.util.GrunnerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("file")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileARFilterAPIRunner extends BaseARFilterAPIRunner {

    private final ARServerUser arServerUser;
    private final GroovyScriptEngine groovyScriptEngine;

    @Override
    public List<Value> run(ARPluginContext context, List<Value> values, Map<String, String> args) throws GrunnerException {
        String scriptFileName = args.get("name");
        GrunnerUtil.validateArg(scriptFileName, "name");
        log.info("run file script: {}", scriptFileName);
        try {
            GrunnerARFilterAPIPlugin script = (GrunnerARFilterAPIPlugin) groovyScriptEngine
                    .createScript(scriptFileName, getBinding(arServerUser));
            return script.filterAPICall(context, values);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            log.error(errorMsg, e);
            throw new GrunnerException("error occurred while executing file script: " + errorMsg);
        }
    }
}
