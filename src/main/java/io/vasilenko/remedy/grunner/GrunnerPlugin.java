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
import com.bmc.arsys.pluginsvr.plugins.ARFilterAPIPlugin;
import com.bmc.arsys.pluginsvr.plugins.ARPluginContext;
import com.google.inject.Guice;
import com.google.inject.Inject;
import io.vasilenko.remedy.grunner.di.InjectorModule;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.service.GrunnerService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GrunnerPlugin extends ARFilterAPIPlugin {

    private static final int VALUES_VALID_SIZE = 2;
    private static final int ACTION_VALUE_INDEX = 0;

    @Inject
    private Map<Value, GrunnerService> serviceMap;

    @Override
    public void initialize(ARPluginContext context) {
        Guice.createInjector(new InjectorModule()).injectMembers(this);
        log.debug("GrunnerPlugin initialized");
    }

    @Override
    public List<Value> filterAPICall(ARPluginContext context, List<Value> values) throws GrunnerException {
        List<Value> result;
        log.debug("filterAPICall values: {}", values);
        if (validateValues(values)) {
            GrunnerService service = serviceMap.get(values.get(ACTION_VALUE_INDEX));
            List<Value> args = new ArrayList<>(values);
            args.remove(ACTION_VALUE_INDEX);
            result = service.run(args);
        } else {
            throw new GrunnerException("invalid input arguments");
        }
        log.debug("result: {}", result);
        return result;
    }

    private boolean validateValues(List<Value> values) {
        return values.size() == VALUES_VALID_SIZE;
    }
}
