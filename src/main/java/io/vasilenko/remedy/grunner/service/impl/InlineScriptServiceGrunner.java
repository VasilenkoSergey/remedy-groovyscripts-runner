package io.vasilenko.remedy.grunner.service.impl;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.google.inject.Inject;
import groovy.lang.GroovyShell;
import io.vasilenko.remedy.grunner.service.GrunnerPluginService;

import java.util.ArrayList;
import java.util.List;

public class InlineScriptServiceGrunner implements GrunnerPluginService {

    private final GroovyShell shell;

    @Inject
    public InlineScriptServiceGrunner(GroovyShell shell) {
        this.shell = shell;
    }

    @Override
    public List<Value> run(List<Value> values) throws ARException {
        return new ArrayList<>();
    }
}
