package com.dubbo.core.example;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

public class AkkaStreamExample {

    public static void main(String[] args) {
        ActorSystem test = ActorSystem.create("test");

        Source<Integer, NotUsed> source = Source.range(1, 100000);
        CompletionStage<Done> doneCompletionStage = source.runForeach(i -> {
            System.out.println(i);
        }, test);

        Source<BigInteger, NotUsed> scan = source.scan(BigInteger.ONE, (v1, v2) -> {
            return v1.multiply(BigInteger.valueOf(v2));
        });
        scan.map(num -> ByteString.fromString(num.toString()+"\n"))
                .runWith(FileIO.toPath(Paths.get("D:\\tool\\test.txt")), test);

        test.terminate();
    }

}
