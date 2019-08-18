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
import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileScriptRunnerTest {

    @InjectMocks
    private FileScriptRunner fileScriptRunner;
    private List<Value> inputValues;

    @Mock
    private GroovyScriptEngine engine;
    @Mock
    private Script script;

    @Before
    public void setUp() {
        inputValues = new ArrayList<>();
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveInvalidInputValuesSize() throws ARException {
        fileScriptRunner.run(inputValues);
    }

    @Test(expected = ARException.class)
    public void throwARExceptionWhenFailedToCreateScript() throws GrunnerException, ResourceException, ScriptException {
        initInputValuesWithValidItems();

        when(engine.createScript(anyString(), any(Binding.class))).thenThrow(ResourceException.class);

        fileScriptRunner.run(inputValues);
    }

    @Test
    public void testOutputValuesWhenScriptReturnNull() throws GrunnerException, ResourceException, ScriptException {
        List<Value> outputValues = run(null);

        assertEquals(0, outputValues.size());
    }

    @Test
    public void testOutputValuesWhenScriptReturnNonListObject() throws GrunnerException, ResourceException, ScriptException {
        Object result = "result";

        List<Value> outputValues = run(result);

        assertEquals(0, outputValues.size());
    }

    @Test
    public void testOutputValuesWhenScriptReturnListWithAllValueElements() throws GrunnerException, ResourceException, ScriptException {
        Object result = new ArrayList<>(Arrays.asList(new Value("First"), new Value("Second")));

        List<Value> outputValues = run(result);

        assertEquals(2, outputValues.size());
    }

    @Test
    public void testOutputValuesWhenScriptReturnListWithValueAndNotValueElements() throws GrunnerException, ResourceException, ScriptException {
        Object result = new ArrayList<>(Arrays.asList(new Value("First"), "Second"));

        List<Value> outputValues = run(result);

        assertEquals(1, outputValues.size());
    }

    @Test
    public void testOutputValuesWhenScriptReturnEmptyList() throws GrunnerException, ResourceException, ScriptException {
        Object result = new ArrayList<>(Collections.emptyList());

        List<Value> outputValues = run(result);

        assertEquals(0, outputValues.size());
    }

    private void initInputValuesWithValidItems() {
        inputValues.add(new Value("FILE"));
        inputValues.add(new Value("ScriptName"));
    }

    private List<Value> run(Object result) throws ResourceException, ScriptException, GrunnerException {
        initInputValuesWithValidItems();
        when(engine.createScript(anyString(), any(Binding.class))).thenReturn(script);
        when(script.run()).thenReturn(result);
        return fileScriptRunner.run(inputValues);
    }
}