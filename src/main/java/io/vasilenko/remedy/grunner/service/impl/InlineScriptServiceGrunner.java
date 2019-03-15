package io.vasilenko.remedy.grunner.service.impl;

import com.bmc.arsys.api.Value;
import com.google.inject.Inject;
import groovy.lang.GroovyShell;
import io.vasilenko.remedy.grunner.service.GrunnerPluginService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InlineScriptServiceGrunner implements GrunnerPluginService {

    private static final int SCRIPT_TEXT_VALUE = 0;

    private final GroovyShell shell;

    @Inject
    public InlineScriptServiceGrunner(GroovyShell shell) {
        this.shell = shell;
    }

    @Override
    public List<Value> run(List<Value> values) {
        String script = String.valueOf(values.get(SCRIPT_TEXT_VALUE));
        log.debug("inline script run: {}", script);
        Object result = shell.evaluate(script);
        log.debug("inline script result: {}", result);
        return new ArrayList<>();
    }
}
