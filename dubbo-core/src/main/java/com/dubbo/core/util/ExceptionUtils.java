package com.dubbo.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public class ExceptionUtils {

    public static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}
