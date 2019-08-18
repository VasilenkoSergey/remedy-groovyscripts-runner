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
import com.bmc.thirdparty.org.slf4j.Logger;
import com.bmc.thirdparty.org.slf4j.LoggerFactory;
import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.service.BaseAbstractScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.vasilenko.remedy.grunner.util.GrunnerUtil.validateInputValuesSize;

@Service("fileScriptRunner")
public class FileScriptRunner extends BaseAbstractScriptRunner {

    private static final int EXPECTED_INPUT_VALUES_SIZE = 1;
    private static final int SCRIPT_NAME_VALUE_INDEX = 0;

    private final Logger log = LoggerFactory.getLogger(FileScriptRunner.class);

    private final ARServerUser arServerUser;
    private final ApplicationContext appContext;
    private final GroovyScriptEngine engine;

    @Autowired
    public FileScriptRunner(ARServerUser arServerUser, ApplicationContext appContext, GroovyScriptEngine engine) {
        this.arServerUser = arServerUser;
        this.appContext = appContext;
        this.engine = engine;
    }

    @Override
    public List<Value> run(List<Value> inputValues) throws GrunnerException {
        validateInputValuesSize(inputValues.size(), EXPECTED_INPUT_VALUES_SIZE);
        String scriptFileName = String.valueOf(inputValues.get(SCRIPT_NAME_VALUE_INDEX));
        log.debug("run file script: {}", scriptFileName);
        List<Value> values;
        try {
            Script script = engine.createScript(scriptFileName + ".groovy", getBindingForFileScript(scriptFileName, inputValues));
            values = runScript(script);
        } catch (ResourceException | ScriptException e) {
            String errorMsg = e.getMessage();
            log.error(errorMsg, e);
            throw new GrunnerException("Error occurred while loading file script: " + errorMsg);
        }
        return values;
    }

    private Binding getBindingForFileScript(String scriptName, List<Value> inputValues) {
        Logger scriptLogger = LoggerFactory.getLogger("io.vasilenko.remedy.grunner.scripts.file." + scriptName);
        return getBinding(scriptLogger, inputValues, arServerUser, appContext);
    }
}
