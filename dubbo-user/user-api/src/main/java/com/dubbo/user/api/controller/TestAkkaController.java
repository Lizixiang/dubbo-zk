package com.dubbo.user.api.controller;

import akka.Done;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.dubbo.core.exception.Result;
import com.dubbo.user.api.dto.TestVo;
import com.dubbo.user.entity.UserState;
import com.dubbo.user.service.UserStateService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/akka")
public class TestAkkaController {

    private static final Logger logger = LoggerFactory.getLogger(TestAkkaController.class);

//    @Autowired
//    private ActorSystem actorSystem;
//
//    @Autowired
//    private SpringExt springExt;
//
//
//    @GetMapping("/test1")
//    public String test1() {
//        ActorRef testActor = actorSystem.actorOf(springExt.props("TestActor"));
//        testActor.tell("hello akka!", ActorRef.noSender());
//        return "OK";
//    }

    @GetMapping("/test")
    public String test() throws IOException {
        File file = FileUtils.getFile("D:\\tool\\test.txt");

        System.out.println(file.getName());

        String data = FileUtils.readFileToString(file, Charset.defaultCharset());
        System.out.println(data);
        return data;
    }

    @GetMapping("/test1")
    public String test1() throws IOException {
        Sink<ByteString, CompletionStage<Done>> sink = Sink.<ByteString>foreach(chunk -> {
            System.out.println(chunk.utf8String());
        });
        CompletionStage<IOResult> test1 = FileIO.fromPath(Paths.get("D:\\tool\\test.txt")).to(sink).run(ActorSystem.create("test1"));
        return "OK";
    }

    @Autowired
    private UserStateService userStateService;

    private final ConcurrentLinkedQueue<TestVo> queue = new ConcurrentLinkedQueue();

    @GetMapping("easyexcel")
    public Result easyexcel() {
        String path = "D:\\tool\\百万数据.xlsx";
        // 将excel中数据分成多个sheet页
        int sheetNum = 10;
        // 创建sheet个数线程的线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(sheetNum, sheetNum,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        // 使用easyexcel流式读取大文件,每个线程负责处理一个sheet页
        for (int i = 0; i < sheetNum; i++) {
            int finalI = i;
            executor.submit(() -> {
//                logger.info("start read sheet[{}]", finalI);
                EasyExcel.read(path, TestVo.class, new MyDataListener(userStateService))
                        .sheet(finalI).doRead();
//                logger.info("start read sheet[{}]", finalI);
            });
        }
        executor.shutdown();
        try {
            // 等待线程池中任务全部执行完毕或者超时中断
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Result.SUCCESS();
    }

    class MyDataListener implements ReadListener<TestVo> {
        private final Logger logger = LoggerFactory.getLogger(MyDataListener.class);

        public final Integer BATCH_SIZE = 1000;

        private UserStateService userStateService;

        public MyDataListener(UserStateService userStateService) {
            this.userStateService = userStateService;
        }

        /**
         * 监听数据读取成功
         *
         * @param testVo          one row value. It is same as {@link AnalysisContext#readRowHolder()}
         * @param analysisContext analysis context
         */
        @Override
        public void invoke(TestVo testVo, AnalysisContext analysisContext) {
//            logger.info("listen data:{}", JSON.toJSONString(testVo));
            // 使用线程安全的队列存放临时数据
            queue.add(testVo);
            // 当达到1000个，批量插入数据库
            if (queue.size() >= BATCH_SIZE) {
                process(null);
            }
        }

        /**
         * 所有数据读取完成后调用此方法
         *
         * @param analysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            logger.info("read completed");
            // 处理队里中剩余的数据
            if (queue.size() > 0) {
                process(true);
            }
        }

        private void process(Boolean processAll) {
            synchronized (MyDataListener.class) {
                try {
//                    logger.info("oper before queue size：{}", queue.size());
                    if (Optional.ofNullable(processAll).orElse(Boolean.FALSE)) {
                        List<UserState> userStates = queue.stream().map(e -> new UserState(e.getUid(), null)).collect(Collectors.toList());
                        userStateService.batchInsert(userStates);
                        queue.clear();
//                        logger.info("userStateService.batchInsert userStates size：{}", userStates.size());
                    } else if (queue.size() >= BATCH_SIZE) {
                        // 默认取队列前1000个
                        List<TestVo> testVos = queue.stream().limit(BATCH_SIZE).collect(Collectors.toList());
//                        logger.info("testVos size：{}", testVos.size());
                        List<UserState> userStates = testVos.stream().map(e -> new UserState(e.getUid(), null)).collect(Collectors.toList());
                        userStateService.batchInsert(userStates);
                        queue.removeAll(testVos);
//                        logger.info("userStateService.batchInsert testVos size：{}", testVos.size());
                    }
//                    logger.info("oper after queue size：{}", queue.size());
                } catch (Exception e) {
                    // 抛出异常， 生产中可以记录相关日志表
                    logger.error("invoke error:{}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }

}
