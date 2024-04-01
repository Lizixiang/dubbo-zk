package com.dubbo.core.permission;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
// 指名数据库查询方法需要和权限挂钩
public @interface Permission {

    // TODO: 2024/3/31 查询负责跟进的客户id、组长可以看到组员的全部数据
    // TODO: 2024/3/31 查询本部门的文档、或者设为隐私的文档，无法查看别人设为隐私的文档，但是部门领导可以看到所有文档

}
