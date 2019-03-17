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

import com.bmc.arsys.api.Value;
import groovy.lang.GroovyShell;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InlineScriptServiceTest {

    @Mock private GroovyShell shell;
    @InjectMocks InlineScriptService service;
    private List<Value> values;

    @Before
    public void setUp() {
        values = new ArrayList<>();
    }

    @Test
    public void serviceMustExecuteScript() {
        String script = "1 + 2";
        values.add(new Value(script));

        when(shell.evaluate(String.valueOf(values.get(0)))).thenReturn("3");

        List<Value> outputValues = service.run(values);

        verify(shell, times(1)).evaluate(script);
        assertEquals(1, outputValues.size());
        assertEquals(new Value("3"), outputValues.get(0));
    }
}
