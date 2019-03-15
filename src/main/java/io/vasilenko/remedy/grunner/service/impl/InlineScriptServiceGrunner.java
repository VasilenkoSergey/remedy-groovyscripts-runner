package io.vasilenko.remedy.grunner.service.impl;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.google.inject.Inject;
import groovy.lang.GroovyShell;
import io.vasilenko.remedy.grunner.service.GrunnerPluginService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InlineScriptServiceGrunner implements GrunnerPluginService {

    private static final int SCRIPT_VALUE = 0;

    private final GroovyShell shell;

    @Inject
    public InlineScriptServiceGrunner(GroovyShell shell) {
        this.shell = shell;
    }

    @Override
    public List<Value> run(List<Value> values) throws ARException {
        String script = String.valueOf(values.get(SCRIPT_VALUE));
        log.debug("run inline script: {}", script);
        shell.evaluate(script);
        return new ArrayList<>();
    }
}
