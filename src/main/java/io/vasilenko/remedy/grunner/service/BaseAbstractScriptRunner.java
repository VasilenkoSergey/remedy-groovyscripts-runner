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

package io.vasilenko.remedy.grunner.service;

import com.bmc.arsys.api.ARServerUser;
import com.bmc.arsys.api.Value;
import com.bmc.thirdparty.org.slf4j.Logger;
import groovy.lang.Binding;
import groovy.lang.Script;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAbstractScriptRunner implements ScriptRunner {

    protected List<Value> runScript(Script script) {
        Object scriptOutputObject = script.run();
        List<Value> outputValues = new ArrayList<>();

        if (scriptOutputObject != null) {
            String scriptOutputObjectType = scriptOutputObject.getClass().getTypeName();
            if (scriptOutputObjectType.equals("java.util.ArrayList")) {
                List list = (List) scriptOutputObject;
                if (!list.isEmpty()) {
                    for (Object object : list) {
                        String itemType = object.getClass().getTypeName();
                        if (itemType.equals("com.bmc.arsys.api.Value")) {
                            Value value = (Value) object;
                            outputValues.add(value);
                        }
                    }
                }
            }
        }
        return outputValues;
    }

    protected Binding getBinding(Logger logger, List<Value> values, ARServerUser arServerUser, ApplicationContext context) {
        Binding binding = new Binding();
        binding.setVariable("arServerUser", arServerUser);
        binding.setVariable("inputValues", values);
        binding.setVariable("log", logger);
        binding.setVariable("appContext", context);
        return binding;
    }
}
