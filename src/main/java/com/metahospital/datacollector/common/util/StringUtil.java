package com.metahospital.datacollector.common.util;

import com.metahospital.datacollector.aop.handler.CollectorException;
import com.metahospital.datacollector.common.RestCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wanghaoyuan
 */
public class StringUtil {
    public static int calcLength(String s) {
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
        return s.getBytes().length;
    }

    public static void checkStringNotEmptyWithLengthLimit(String pidAddress, int pidAddressLengthLimit, String tips) {
        if (StringUtils.isEmpty(pidAddress)) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("%s不能为空", tips));
        }
        if (StringUtil.calcLength(pidAddress) > pidAddressLengthLimit) {
            throw new CollectorException(RestCode.PARAM_INVALID_ERR, String.format("%s长度不能超过%d", tips, pidAddressLengthLimit));
        }
    }
}
