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

package io.vasilenko.remedy.grunner.exception;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Constants;
import com.bmc.arsys.api.StatusInfo;

import java.util.ArrayList;
import java.util.List;

public class GrunnerException extends ARException {

    private static final int ERROR_MSG_NUMBER = 10000;

    public GrunnerException(String message) {
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setMessageType(Constants.AR_RETURN_ERROR);
        statusInfo.setMessageNum(ERROR_MSG_NUMBER);
        statusInfo.setMessageText(message);
        List<StatusInfo> statusInfoList = new ArrayList<>();
        statusInfoList.add(statusInfo);
        this.setLastStatus(statusInfoList);
    }
}
