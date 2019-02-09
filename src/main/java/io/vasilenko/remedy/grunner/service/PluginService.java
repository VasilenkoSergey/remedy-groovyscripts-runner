package io.vasilenko.remedy.grunner.service;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;

import java.util.List;

public interface PluginService {

    List<Value> run(List<Value> values) throws ARException;
}
