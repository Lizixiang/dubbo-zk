# dubbo-zk
基于dubbo+zk脚手架


# unfinished issues
- [x] zookeeper保证分布式事务的最终一致性
- [x] zookeeper分布式锁
- [x] zookeeper config配置中心：动态配置数据(缺点：1.zk没有自带的web 2.只能借助原生api实现 3.需要实现watcher和publisher)
- [ ] zookeeper负载均衡：根据自定义负载均衡算法选出空闲的机器（负载均衡算法：1.轮训算法 2.负载量算法）
- [ ] 集成elastic job
- [ ] 增加pulsar功能：解决分布式事务问题
- [x] 解决无法捕获dubbo rpc抛出的异常
- [ ] canal+kafka监控mysql binlog日志实现数据同步
- [ ] 引入普罗米修斯监控，并配合Grafana做前端展示
- [ ] 引入apache dubbo动态配置中心（zk、apollo、nacos可选）
- [ ] 引入雪花算法snowFlake
- [x] 引入zk分布式锁注解(zk不支持延迟超时时间，注解支持spel表达式)
- [x] 加入全局异常
- [x] 限流算法demo：滑动窗口、漏桶算法、令牌桶算法
- [ ] akka stream读取大文件
- [ ] 我的第一个netty应用
- [x] 研究责任链模式在spring中的应用以及造第一个轮子
- [ ] 研究观察者模式在spring中的应用以及造第一个轮子
- [ ] 研究dubbo spi机制以及造第一个轮子
- [x] 研究spring国际化并写第一个demo
- [ ] 研究guava的理论基础并实践应用场景、了解caffine的技术特点和简单demo
- [ ] 研究抽奖算法的几种实现方式、使用场景、时间复杂度、测试报告
