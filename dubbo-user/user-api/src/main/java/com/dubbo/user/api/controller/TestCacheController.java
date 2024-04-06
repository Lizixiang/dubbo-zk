package com.dubbo.user.api.controller;

import com.alibaba.fastjson.JSON;
import com.dubbo.core.exception.Result;
import com.dubbo.core.util.RedisUtils;
import com.dubbo.user.entity.SysUser;
import com.dubbo.user.mapper.UserMapper;
import com.dubbo.user.service.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <pre>
 * 此处填写中文类名
 * 此处填写中文类描述
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月06日
 */
@RestController
@RequestMapping("/test/cache")
public class TestCacheController {

    private static final Logger logger = LoggerFactory.getLogger(TestCacheController.class);

//    @Autowired
//    private Cache cache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;

//    @GetMapping("/test1")
//    public Result test1(Integer userId) {
//        String key = "user_" + userId;
//        SysUser user = (SysUser) cache.get(key, k -> {
//            String s = redisUtils.get(k.toString());
//            if (StringUtils.isNoneBlank(s)) {
//                logger.info("get result from redis:{}", s);
//                return JSON.parseObject(s, SysUser.class);
//            }
//            // TODO: 2024/4/3 缓存穿透
//            SysUser sysUser = userMapper.selectById(userId);
//            if (sysUser != null) {
//                logger.info("get result from db:{}", JSON.toJSONString(sysUser));
//                redisUtils.set(k.toString(), JSON.toJSONString(sysUser));
//                redisUtils.expire(k.toString(), 180);
//                return sysUser;
//            }
//            return null;
//        });
//        return Result.SUCCESS(user);
//    }
//
//    @GetMapping("/test2")
//    public Result test2(Integer userId, String name) {
//        String key = "user_" + userId;
//        SysUser sysUser = userMapper.selectById(userId);
//        if (sysUser != null) {
//            sysUser.setName(name);
//            // 更新db
//            userMapper.updateById(sysUser);
//            // 更新redis
//            redisUtils.set(key, JSON.toJSONString(sysUser));
//            // 更新caffeine
//            cache.put(key, sysUser);
//        }
//        return Result.SUCCESS();
//    }
//
//    @GetMapping("/test3")
//    public Result test3(Integer userId) {
//        String key = "user_" + userId;
//        userMapper.deleteById(userId);
//        // 删除缓存
//        redisUtils.del(key);
//        // 删除caffeine内存
//        cache.invalidate(key);
//        return Result.SUCCESS();
//    }

    @GetMapping("/test4")
    public Result test4() {
        List<SysUser> sysUsers = userService.queryAll();
        return Result.SUCCESS(sysUsers);
    }

    @GetMapping("/test5")
    public Result test5(Integer userId) {
        return Result.SUCCESS(userService.queryById(userId));
    }

    @GetMapping("/test6")
    public Result test6(Integer userId, String name) {
        return Result.SUCCESS(userService.updateNameById(userId, name));
    }

}
