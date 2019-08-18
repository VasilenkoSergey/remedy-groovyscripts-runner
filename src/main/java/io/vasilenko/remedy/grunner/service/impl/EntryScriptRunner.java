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
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import io.vasilenko.remedy.grunner.service.ScriptRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("entryScriptRunner")
public class EntryScriptRunner implements ScriptRunner {

    @Override
    public List<Value> run(List<Value> values) throws GrunnerException {
        throw new GrunnerException("Not supported yet.");
    }
}
