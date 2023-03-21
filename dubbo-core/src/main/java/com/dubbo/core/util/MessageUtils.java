package com.dubbo.core.util;

import com.dubbo.core.exception.ResultCode;
import com.dubbo.core.holder.SpringApplicationContextHolder;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @author lizixiang
 * @date 2023年03月21日
 */
public class MessageUtils {

    public static String getMessage(ResultCode resultCode) {
        return getMessage(resultCode, LocaleContextHolder.getLocale());
    }

    public static String getMessage(ResultCode resultCode, Object... args) {
        return getMessage(resultCode, LocaleContextHolder.getLocale(), args);
    }

    public static String getMessage(ResultCode resultCode, Locale locale) {
        return getMessage(resultCode, locale, null);
    }

    public static String getMessage(ResultCode resultCode, Locale locale, Object... args) {
        return SpringApplicationContextHolder.getApplicationContext().getMessage(String.valueOf(resultCode.getCode()), args, resultCode.getMessage(), locale);
    }

}
