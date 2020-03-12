package io.vasilenko.remedy.grunner.script;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrunnerARFilterAPIPluginTest {

    @Spy
    private GrunnerARFilterAPIPlugin script;

    @Test
    public void run() {
        when(script.run()).thenReturn(null);

        Object result = script.run();

        assertNull(result);
    }
}