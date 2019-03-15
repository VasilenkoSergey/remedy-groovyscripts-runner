package io.vasilenko.remedy.grunner;

import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.google.inject.Guice;
import com.google.inject.Inject;
import io.vasilenko.remedy.grunner.di.InjectorModule;
import io.vasilenko.remedy.grunner.service.GrunnerPluginService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GrunnerPlugin extends ARFilterAPIPlugin {

    private static final int ACTION_VALUE_INDEX = 0;

    @Inject
    private Map<Value, GrunnerPluginService> serviceMap;

    @Override
    public void initialize(ARPluginContext context) {
        Guice.createInjector(new InjectorModule()).injectMembers(this);
        log.debug("GrunnerPlugin initialized");
    }

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> values) {
        log.debug("filterAPICall values: {}", values);
        GrunnerPluginService service = serviceMap.get(values.get(ACTION_VALUE_INDEX));

        List<Value> args = new ArrayList<>(values);
        args.remove(ACTION_VALUE_INDEX);

        List<Value> result = service.run(args);
        log.debug("result: {}", result);
        return result;
    }
}
