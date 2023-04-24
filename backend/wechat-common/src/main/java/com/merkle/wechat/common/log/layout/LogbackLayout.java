package com.merkle.wechat.common.log.layout;

import java.util.HashMap;
import java.util.Map;

import com.merkle.wechat.common.exception.BaseException;
import com.merkle.wechat.common.log.SystemServiceLog;
import com.merkle.wechat.common.util.JSONUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

/**
 * Ï
 * 
 * @author tyao
 *
 */
public class LogbackLayout extends LayoutBase<ILoggingEvent> {

    @Override
    public String doLayout(ILoggingEvent event) {
        SystemServiceLog serviceLog = new SystemServiceLog();
        String level = convertLevel(event.getLevel().toString().toLowerCase());
        String msg = event.getFormattedMessage();
        String logger = event.getLoggerName();
        serviceLog.setLevel(level);
        serviceLog.setMessage(msg);
        serviceLog.setCategory(logger);
        serviceLog.setContext(generateContext(serviceLog, event));
        return JSONUtil.objectJsonStr(serviceLog) + CoreConstants.LINE_SEPARATOR;
    }

    private Map<String, Object> generateContext(SystemServiceLog serviceLog, ILoggingEvent event) {
        Map<String, Object> context = new HashMap<String, Object>();
        if (null != event.getArgumentArray()) {
            int i = 1;
            for (Object object : event.getArgumentArray()) {
                context.put("" + i++, object);
            }
        }

        IThrowableProxy ithrowableProxy = event.getThrowableProxy();
        if (ithrowableProxy != null) {
            serviceLog.setBacktrace(ThrowableProxyUtil.asString(ithrowableProxy));
            ThrowableProxy throwableProxy = (ThrowableProxy) ithrowableProxy;
            Throwable throwable = throwableProxy.getThrowable();
            if (throwable instanceof BaseException) {
                BaseException baseException = (BaseException) throwableProxy.getThrowable();
                context.put("0", baseException.getDetails());
            } else {
                throwable.printStackTrace();
            }
        }

        return context;
    }

    private String convertLevel(String level) {
        switch (level) {
            case "warn":
                return "warning";
            case "trace":
            case "all":
                return "debug";
            case "off":
                return "error";
            default:
                return level;
        }
    }

}
