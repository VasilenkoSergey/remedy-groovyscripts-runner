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

package io.vasilenko.remedy.grunner;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.bmc.thirdparty.org.slf4j.Logger;
import com.bmc.thirdparty.org.slf4j.LoggerFactory;
import io.vasilenko.remedy.grunner.config.ScriptType;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.service.ScriptRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.vasilenko.remedy.grunner.util.GrunnerUtil.validateInputValuesSize;

@Configuration
@ComponentScan
public class GrunnerPlugin extends ARFilterAPIPlugin {

    private static final int EXPECTED_INPUT_VALUES_SIZE = 1;
    private static final int SCRIPT_SOURCE_INPUT_INDEX = 0;

    private final Logger log = LoggerFactory.getLogger(GrunnerPlugin.class);

    private AnnotationConfigApplicationContext applicationContext;
    private Map<Value, ScriptRunner> serviceMap;

    @Resource(name = "serviceMap")
    public void setServiceMap(Map<Value, ScriptRunner> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void initialize(ARPluginContext context) {
        applicationContext = new AnnotationConfigApplicationContext(GrunnerPlugin.class);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        log.debug("GrunnerPlugin initialized");
    }

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> inputValues) throws ARException {
        log.debug("input values: {}", inputValues);
        validateInputValues(inputValues);
        Value scriptSourceValue = inputValues.get(SCRIPT_SOURCE_INPUT_INDEX);
        inputValues.remove(SCRIPT_SOURCE_INPUT_INDEX);
        ScriptRunner scriptRunner = serviceMap.get(scriptSourceValue);
        List<Value> result = scriptRunner.run(inputValues);
        log.debug("result: {}", result);
        return result;
    }

    @Override
    public void terminate(ARPluginContext context) {
        applicationContext.close();
    }

    private void validateInputValues(List<Value> inputValues) throws GrunnerException {
        validateInputValuesSize(inputValues.size(), EXPECTED_INPUT_VALUES_SIZE);
        String scriptTypeValue = String.valueOf(inputValues.get(SCRIPT_SOURCE_INPUT_INDEX));
        validateScriptType(scriptTypeValue);
    }

    private void validateScriptType(String scriptTypeValue) throws GrunnerException {
        if (!checkExistingScriptType(scriptTypeValue)) {
            throw new GrunnerException("Invalid input script type value");
        }
    }

    public boolean checkExistingScriptType(String scriptTypeValue) {
        return Arrays.stream(ScriptType.values()).anyMatch(scriptType -> scriptType.name().equals(scriptTypeValue));
    }
}
