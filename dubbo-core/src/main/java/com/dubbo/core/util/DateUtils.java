package com.dubbo.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * @author lizixiang
 * @since 2021/11/25
 */
public class DateUtils {

    public static String formatDurationWords(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
        String duration = DurationFormatUtils.formatDuration(durationMillis, "d' 天 'H' 小时 'm' 分钟 's' 秒'");
        String tmp;
        if (suppressLeadingZeroElements) {
            duration = " " + duration;
            tmp = org.apache.commons.lang3.StringUtils.replaceOnce(duration, " 0 天", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = org.apache.commons.lang3.StringUtils.replaceOnce(tmp, " 0 小时", "");
                if (tmp.length() != duration.length()) {
                    tmp = org.apache.commons.lang3.StringUtils.replaceOnce(tmp, " 0 分钟", "");
                    duration = tmp;
                    if (tmp.length() != tmp.length()) {
                        duration = org.apache.commons.lang3.StringUtils.replaceOnce(tmp, " 0 秒", "");
                    }
                }
            }

            if (!duration.isEmpty()) {
                duration = duration.substring(1);
            }
        }

        if (suppressTrailingZeroElements) {
            tmp = org.apache.commons.lang3.StringUtils.replaceOnce(duration, " 0 秒", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = org.apache.commons.lang3.StringUtils.replaceOnce(tmp, " 0 分钟", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = org.apache.commons.lang3.StringUtils.replaceOnce(tmp, " 0 小时", "");
                    if (tmp.length() != duration.length()) {
                        duration = org.apache.commons.lang3.StringUtils.replaceOnce(tmp, " 0 days", "");
                    }
                }
            }
        }

        duration = " " + duration;
        duration = org.apache.commons.lang3.StringUtils.replaceOnce(duration, " 1 秒", " 1 秒");
        duration = org.apache.commons.lang3.StringUtils.replaceOnce(duration, " 1 分钟", " 1 分钟");
        duration = org.apache.commons.lang3.StringUtils.replaceOnce(duration, " 1 小时", " 1 小时");
        duration = StringUtils.replaceOnce(duration, " 1 天", " 1 天");
        return duration.trim();
    }

}
