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

package io.vasilenko.remedy.grunner.util;

import com.bmc.arsys.api.ARException;
import io.vasilenko.remedy.grunner.exception.GrunnerException;
import org.junit.Test;

import static io.vasilenko.remedy.grunner.util.GrunnerUtil.validateArg;

public class GrunnerUtilTest {

    @Test(expected = ARException.class)
    public void failIfArgIsNull() throws GrunnerException {
        validateArg(null, "source");
    }
}