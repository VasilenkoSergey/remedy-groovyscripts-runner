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
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import io.vasilenko.remedy.grunner.service.ARFilterAPIRunner;
import io.vasilenko.remedy.grunner.service.impl.EntryARFilterAPIRunner;
import io.vasilenko.remedy.grunner.service.impl.FileARFilterAPIRunner;
import io.vasilenko.remedy.grunner.service.impl.InlineARFilterRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrunnerPluginTest {

    @InjectMocks
    private GrunnerPlugin plugin;
    @Mock
    private Map<String, ARFilterAPIRunner> serviceMap;
    @Mock
    private ARPluginContext arPluginContext;
    @Mock
    private FileARFilterAPIRunner fileGroovyARFilterAPIRunner;
    @Mock
    private EntryARFilterAPIRunner entryGroovyARFilterAPIRunner;
    @Mock
    private InlineARFilterRunner inlineScriptRunner;

    private List<Value> inputValues;

    @Before
    public void setUp() {
        inputValues = new ArrayList<>();
        when(serviceMap.get("file")).thenReturn(fileGroovyARFilterAPIRunner);
        when(serviceMap.get("entry")).thenReturn(entryGroovyARFilterAPIRunner);
        when(serviceMap.get("inline")).thenReturn(inlineScriptRunner);
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveNullInputValuesSize() throws ARException {
        plugin.filterAPICall(arPluginContext, null);
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveInvalidInputValuesSize() throws ARException {
        plugin.filterAPICall(arPluginContext, inputValues);
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveEmptyInputArgs() throws ARException {
        inputValues.add(new Value(""));

        plugin.filterAPICall(arPluginContext, inputValues);
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveInvalidInputArgs() throws ARException {
        inputValues.add(new Value("source:"));

        plugin.filterAPICall(arPluginContext, inputValues);
    }

    @Test
    public void runFileScriptRunnerWhenSourceIsFile() throws ARException {
        inputValues.add(new Value("source:file,name:Sample.groovy"));
        Map<String, String> args = new HashMap<>();
        args.put("source", "file");
        args.put("name", "Sample.groovy");

        plugin.filterAPICall(arPluginContext, inputValues);

        verify(fileGroovyARFilterAPIRunner).run(arPluginContext, inputValues, args);
    }

    @Test
    public void runEntryScriptRunnerWhenFirstSourceIsEntry() throws ARException {
        inputValues.add(new Value("source:entry,name:Sample.groovy"));
        Map<String, String> args = new HashMap<>();
        args.put("source", "entry");
        args.put("name", "Sample.groovy");

        plugin.filterAPICall(arPluginContext, inputValues);

        verify(entryGroovyARFilterAPIRunner).run(arPluginContext, inputValues, args);
    }

    @Test
    public void runInlineScriptRunnerWhenSourceIsInline() throws ARException {
        inputValues.add(new Value("source:inline,name:Sample.groovy"));
        Map<String, String> args = new HashMap<>();
        args.put("source", "inline");
        args.put("name", "Sample.groovy");

        plugin.filterAPICall(arPluginContext, inputValues);

        verify(inlineScriptRunner).run(arPluginContext, inputValues, args);
    }
}
