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

import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.service.GrunnerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GrunnerPluginTest {

    @Mock private ARPluginContext context;
    @Mock private GrunnerService service;
    @Mock private Map<Value, GrunnerService> serviceMap;

    @InjectMocks
    private GrunnerPlugin plugin;
    private List<Value> inputValues;
    private List<Value> outputValues;
    private List<Value> values;

    @Before
    public void setUp() {
        inputValues = new ArrayList<>();
        outputValues = new ArrayList<>();
    }

    @Test(expected = GrunnerException.class)
    public void pluginMustThrowsExceptionWhenInvalidInputValuesSize() throws GrunnerException {
        inputValues.add(new Value());

        plugin.filterAPICall(context, inputValues);

        assertEquals(2, inputValues.size());
    }

    @Test
    public void pluginMustCallInlineScriptService() throws GrunnerException {
        String scriptText = "1 + 2";
        inputValues.add(new Value("INLINE"));
        inputValues.add(new Value(scriptText));
        values = Collections.singletonList(new Value(scriptText));

        when(serviceMap.get(inputValues.get(0))).thenReturn(service);
        when(service.run(values)).thenReturn(outputValues);

        plugin.filterAPICall(context, inputValues);

        verify(service, times(1)).run(values);
    }

    @Test
    public void pluginMustCallFileScriptService() throws GrunnerException {
        String scriptName = "Temp.groovy";
        inputValues.add(new Value("FILE"));
        inputValues.add(new Value(scriptName));
        values = Collections.singletonList(new Value(scriptName));

        when(serviceMap.get(inputValues.get(0))).thenReturn(service);
        when(service.run(values)).thenReturn(outputValues);

        plugin.filterAPICall(context, inputValues);

        verify(service, times(1)).run(values);
    }
}
