package com.dubbo.core.util;

import org.springframework.stereotype.Component;

/**
 * @author lizixiang
 * @since 2022/4/25
 */
@Component
public class LuaUtils {

    public String buildSplitFunc() {
        StringBuilder s = new StringBuilder();
        s.append("\n	function string:split(sep)");
        s.append("\n		local sep, fields = sep or \":\", {}");
        s.append("\n		local pattern = string.format(\"([^%s]+)\", sep)");
        s.append("\n		self:gsub(pattern, function (c) fields[#fields + 1] = c end)");
        s.append("\n		return fields");
        s.append("\n	end");
        return s.toString();
    }

}
