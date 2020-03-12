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

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.script.GrunnerARFilterAPIPlugin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileGroovyARFilterAPIRunnerTest {

    @InjectMocks
    private FileARFilterAPIRunner fileGroovyARFilterAPIRunner;
    @Mock
    private ARPluginContext arPluginContext;
    @Mock
    private GroovyScriptEngine engine;
    @Mock
    private GrunnerARFilterAPIPlugin script;

    private List<Value> inputValues;
    private List<Value> outputValues;

    @Before
    public void setUp() {
        inputValues = new ArrayList<>();
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveInvalidInputArgs() throws ARException {
        Map<String, String> args = new HashMap<>();
        args.put("source", "file");
        args.put("name", null);

        fileGroovyARFilterAPIRunner.run(arPluginContext, inputValues, args);
    }

    @Test(expected = ARException.class)
    public void throwARExceptionWhenFailedToCreateScript() throws GrunnerException {
        Map<String, String> args = new HashMap<>();
        args.put("source", "file");
        args.put("name", "");

        fileGroovyARFilterAPIRunner.run(arPluginContext, inputValues, args);
    }

    @Test
    public void scriptMustReturnResultAsValueList() throws ResourceException, ScriptException, GrunnerException {
        inputValues = Arrays.asList(new Value(3), new Value(7));
        Map<String, String> args = new HashMap<>();
        args.put("source", "file");
        args.put("name", "Sample.groovy");
        List<Value> result = Collections.singletonList(new Value(10));

        when(engine.createScript(eq("Sample.groovy"), any(Binding.class))).thenReturn(script);
        when(script.filterAPICall(arPluginContext, inputValues)).thenReturn(result);

        List<Value> outputValues = fileGroovyARFilterAPIRunner.run(arPluginContext, inputValues, args);

        assertEquals(1, outputValues.size());
        assertEquals(Value.class, outputValues.get(0).getClass());
        assertEquals(10, outputValues.get(0).getIntValue());
    }
}