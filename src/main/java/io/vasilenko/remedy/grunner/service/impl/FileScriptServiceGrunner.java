package io.vasilenko.remedy.grunner.service.impl;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import io.vasilenko.remedy.grunner.service.GrunnerPluginService;

import java.util.ArrayList;
import java.util.List;

public class FileScriptServiceGrunner implements GrunnerPluginService {

    @Override
    public List<Value> run(List<Value> values) throws ARException {
        return new ArrayList<>();
    }
}
