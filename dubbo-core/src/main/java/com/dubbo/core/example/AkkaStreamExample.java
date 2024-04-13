package com.dubbo.core.example;

import com.dubbo.core.util.PoiExceclUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AkkaStreamExample {

    private static final Logger logger = LoggerFactory.getLogger(AkkaStreamExample.class);

    public static void main(String[] args) {
//        ActorSystem test = ActorSystem.create("test");
//
//        Source<Integer, NotUsed> source = Source.range(1, 100000);
//        CompletionStage<Done> doneCompletionStage = source.runForeach(i -> {
//            System.out.println(i);
//        }, test);
//
//        Source<BigInteger, NotUsed> scan = source.scan(BigInteger.ONE, (v1, v2) -> {
//            return v1.multiply(BigInteger.valueOf(v2));
//        });
//        scan.map(num -> ByteString.fromString(num.toString() + "\n"))
//                .runWith(FileIO.toPath(Paths.get("D:\\tool\\test.txt")), test);
//
//        test.terminate();
        poi();
    }

    public static void poi() {
        String[] columns = {"uid"};
        // 百万数据.xlsx文件大小为70M左右，通过main方法上增加vm参数：-Xms1G -Xmx1G，将堆大小设置为1G，但是还是会报堆内存溢出
        List<Map<String, String>> list = PoiExceclUtils.readExcel("D:\\tool\\百万数据.xlsx", columns);
        logger.info("list:{}", list);
    }



}
