package io.vasilenko.remedy.grunner.exception;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.Constants;
import com.bmc.arsys.api.StatusInfo;

import java.util.ArrayList;
import java.util.List;

public class PluginException extends ARException {

    private static final int ERROR_MSG_NUMBER = 10000;

    public PluginException(String message) {
        StatusInfo statusInfo = new StatusInfo();
        statusInfo.setMessageType(Constants.AR_RETURN_ERROR);
        statusInfo.setMessageNum((long) ERROR_MSG_NUMBER);
        statusInfo.setMessageText(message);
        List<StatusInfo> statusInfoList = new ArrayList<>();
        statusInfoList.add(statusInfo);
        this.setLastStatus(statusInfoList);
    }
}
