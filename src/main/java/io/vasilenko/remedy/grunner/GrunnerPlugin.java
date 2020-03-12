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
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.service.ARFilterAPIRunner;
import io.vasilenko.remedy.grunner.util.GrunnerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
@ComponentScan
public class GrunnerPlugin extends ARFilterAPIPlugin {

    private static final int ARGS_INDEX = 0;
    private static final int MIN_INPUT_VALUES = 1;

    @Autowired
    private Map<String, ARFilterAPIRunner> serviceMap;

    @Override
    public void initialize(ARPluginContext context) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(GrunnerPlugin.class);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        log.info("Grunner Plugin initialized");
    }

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> values) throws ARException {
        log.info("user: {}", context.getUser());
        log.debug("values: {}", values);
        validateValues(values);
        Map<String, String> args = getArgsMap(String.valueOf(values.get(ARGS_INDEX)));
        String source = args.get("source");
        GrunnerUtil.validateArg(source, "source");
        ARFilterAPIRunner runner = serviceMap.get(source);
        values.remove(ARGS_INDEX);
        List<Value> out = runner.run(context, values, args);
        log.debug("out: {}", out);
        return out;
    }

    private void validateValues(List<Value> values) throws GrunnerException {
        if (values != null && values.size() >= MIN_INPUT_VALUES) {
            validateArgs(String.valueOf(values.get(ARGS_INDEX)));
        } else {
            throw new GrunnerException("invalid values");
        }
    }

    private void validateArgs(String args) throws GrunnerException {
        if (args.isEmpty()) {
            throw new GrunnerException("invalid values");
        }
    }

    private Map<String, String> getArgsMap(String str) throws GrunnerException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = str.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length < 2) {
                throw new GrunnerException("invalid args");
            }
            map.put(keyValue[0].toLowerCase(), keyValue[1]);
        }
        return map;
    }
}
