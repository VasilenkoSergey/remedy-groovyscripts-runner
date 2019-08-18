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
import io.vasilenko.remedy.grunner.config.ScriptType;
import io.vasilenko.remedy.grunner.service.ScriptRunner;
import io.vasilenko.remedy.grunner.service.impl.EntryScriptRunner;
import io.vasilenko.remedy.grunner.service.impl.FileScriptRunner;
import io.vasilenko.remedy.grunner.service.impl.InlineScriptRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrunnerPluginTest {

    @InjectMocks
    private GrunnerPlugin plugin;
    private List<Value> inputValues;

    @Mock
    private AnnotationConfigApplicationContext applicationContext;
    @Mock
    private Map<Value, ScriptRunner> serviceMap;
    @Mock
    private FileScriptRunner fileScriptRunner;
    @Mock
    private EntryScriptRunner entryScriptRunner;
    @Mock
    private InlineScriptRunner inlineScriptRunner;

    @Before
    public void setUp() {
        inputValues = new ArrayList<>();
        when(serviceMap.get(new Value(ScriptType.FILE.name()))).thenReturn(fileScriptRunner);
        when(serviceMap.get(new Value(ScriptType.ENTRY.name()))).thenReturn(entryScriptRunner);
        when(serviceMap.get(new Value(ScriptType.INLINE.name()))).thenReturn(inlineScriptRunner);
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveInvalidInputValuesSize() throws ARException {
        plugin.filterAPICall(null, inputValues);
    }

    @Test(expected = ARException.class)
    public void failWhenReceiveInvalidInputScriptTypeValue() throws ARException {
        inputValues.add(new Value("FILES"));

        plugin.filterAPICall(null, inputValues);
    }

    @Test
    public void runFileScriptRunnerWhenFirstInputArgumentIsFILE() throws ARException {
        inputValues.add(new Value("FILE"));

        plugin.filterAPICall(null, inputValues);

        verify(fileScriptRunner).run(inputValues);
    }

    @Test
    public void runEntryScriptRunnerWhenFirstInputArgumentIsENTRY() throws ARException {
        inputValues.add(new Value("ENTRY"));

        plugin.filterAPICall(null, inputValues);

        verify(entryScriptRunner).run(inputValues);
    }

    @Test
    public void runInlineScriptRunnerWhenFirstInputArgumentIsINLINE() throws ARException {
        inputValues.add(new Value("INLINE"));

        plugin.filterAPICall(null, inputValues);

        verify(inlineScriptRunner).run(inputValues);
    }

    @Test
    public void closeContextWhenPluginTerminated() {
        plugin.terminate(null);

        verify(applicationContext).close();
    }
}
