package com.dubbo.user.api.controller;

import akka.Done;
import akka.actor.ActorSystem;
import akka.stream.IOResult;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/akka")
public class TestAkkaController {

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

}
