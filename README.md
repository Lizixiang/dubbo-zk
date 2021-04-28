# dubbo-zk
基于dubbo+zk脚手架


# unfinished issues
- [x] zookeeper保证分布式事务的最终一致性
- [x] zookeeper分布式锁
- [x] zookeeper config配置中心：动态配置数据(缺点：1.zk没有自带的web 2.只能借助原生api实现 3.需要实现watcher和publisher)
- [ ] 引入nacos config配置中心
- [ ] zookeeper负载均衡：根据自定义负载均衡算法选出空闲的机器
- [ ] 集成elastic job
- [ ] 增加pulsar功能：解决分布式事务问题
- [ ] 解决无法捕获dubbo rpc抛出的异常
- [ ] canal+kafka监控mysql binlog日志实现数据同步
- [ ] 引入普罗米修斯监控，并配合Grafana做前端展示
- [ ] 引入apache dubbo动态配置中心（zk、apollo、nacos可选）

