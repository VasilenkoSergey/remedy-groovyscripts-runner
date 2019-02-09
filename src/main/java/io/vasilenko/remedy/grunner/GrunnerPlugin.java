package io.vasilenko.remedy.grunner;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Value;
import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;

import java.util.ArrayList;
import java.util.List;

public class GrunnerPlugin extends ARFilterAPIPlugin {

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> inputList) throws ARException {
        return new ArrayList<>();
    }
}
