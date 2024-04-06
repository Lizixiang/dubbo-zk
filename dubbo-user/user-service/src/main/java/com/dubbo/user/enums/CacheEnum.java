package com.dubbo.user.enums;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-05 15:59:53
 */
public enum CacheEnum {

    USER(CacheManagerEnum.USER.getKey(), "user_info_%s", "用户信息缓存", 3600),
    ;

    // 命名空间
    private String nameSpace;

    // 缓存key
    private String key;

    // 缓存名称
    private String name;

    // 缓存key过期时间，单位：秒
    private long expire;

    CacheEnum(String nameSpace, String key, String name, long expire) {
        this.nameSpace = nameSpace;
        this.key = key;
        this.name = name;
        this.expire = expire;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public long getExpire() {
        return expire;
    }

    public String getFormattedKey(Object... args) {
        return String.format(this.key, args);
    }

}
