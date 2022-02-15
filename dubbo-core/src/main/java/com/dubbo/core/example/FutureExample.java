package com.dubbo.core.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author lizixiang
 * @since 2022/2/9
 */
public class FutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // supplyAsync提供返回值
        CompletableFuture<Integer> c1 = CompletableFuture.supplyAsync(() -> {
            return 1;
        });

        // supplyAsync不提供返回值
        CompletableFuture<Void> c2 = CompletableFuture.runAsync(() -> {
            System.out.println(2);
        });

        // thenApply将方法的返回值作为参数传递到回调方法中
        CompletableFuture<Integer> c3 = c1.thenApply(e -> {
            return e;
        });

        // thenAccept同thenApply一样，接收参数，但是没有返回值
        CompletableFuture<Void> c4 = c1.thenAccept(e -> {
            System.out.println(e);
        });

        CompletableFuture<Void> over = c1.thenRun(() -> {
            System.out.println("over");
        });

        System.out.println(c1.get());
        // null
        System.out.println(c2.get());
        System.out.println(c3.get());

        // exceptionally会将方法中抛出的异常作为参数传到回调中
        CompletableFuture.runAsync(() -> {
            int i = 1/1;
        }).exceptionally(e -> {
            System.out.println(100);
            e.printStackTrace();
            return null;
        });

        CompletableFuture<Integer> c5 = CompletableFuture.supplyAsync(() -> {
            return 1;
        });
        // whenComplete有两个参数，第一个是返回值，第二个是异常，如果方法内抛出异常，则e不为空
        CompletableFuture<Integer> c6 = c5.whenComplete((r, e) -> {
            if (e != null) {
                e.printStackTrace();
            }
            System.out.println(r);
        });
        System.out.println(c6.get());

        // handle和whenComplete用法相同，唯一的区别是get()返回值是回调方法返回的值
        CompletableFuture<Integer> c7 = CompletableFuture.supplyAsync(() -> {
            return 10;
        }).handle((r, e) -> {
            return 20;
        });
        System.out.println(c7.get());

        CompletableFuture<String> c8 = CompletableFuture.supplyAsync(() -> {
            return "c8";
        });

        ////////////////////////////////thenCombine、thenAcceptBoth、thenAcceptBoth////////////////////////////////
        // thenCombine当两个任务都执行完，才执行后续方法，两个任务的返回值都会传入回调参数，并且回调有返回值
        CompletableFuture<String> c9 = CompletableFuture.supplyAsync(() -> {
            return "thenCombine";
        }).thenCombine(c8, (a, b) -> {
            return a + "-" + b;
        });
        System.out.println(c9.get());

        // thenAcceptBoth返回值都会传入回调参数，但是回调没有返回值
        CompletableFuture.supplyAsync(() -> {
            return "thenAcceptBoth";
        }).thenAcceptBoth(c8, (a, b) -> {
            System.out.println(a + "-" + b);
        });

        // thenAcceptBoth回调没有入参，没有返回值
        CompletableFuture.supplyAsync(() -> {
            return "thenAcceptBoth";
        }).runAfterBoth(c8, () -> {
            System.out.println("thenAcceptBoth done");
        });

        ////////////////////////////////applyToEither、acceptEither、runAfterEither////////////////////////////////
        CompletableFuture<String> c10 = CompletableFuture.supplyAsync(() -> {
            return "c10";
        });

        // applyToEither将两个任务组合起来，先执行完的任务将返回值传入回调入参，并且有返回值
        CompletableFuture<String> c11 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "applyToEither";
        }).applyToEither(c10, (r) -> {
            return r;
        });
        System.out.println(c11.get());

        // acceptEither将两个任务组合起来，先执行完的任务将返回值传入回调入参，但是没有返回值
        CompletableFuture.supplyAsync(() -> {
            return "acceptEither";
        }).acceptEither(c10, (r) -> {
            System.out.println(r);
        });

        // runAfterEither回调没有入参，没有返回值
        CompletableFuture.supplyAsync(() -> {
            return "";
        }).runAfterEither(c10, () -> {
            System.out.println("runAfterEither done");
        });

        CompletableFuture<String> b1 = CompletableFuture.supplyAsync(() -> {
            return "b1";
        });

        CompletableFuture<String> b2 = CompletableFuture.supplyAsync(() -> {
            return "b2";
        });

        CompletableFuture<String> b3 = CompletableFuture.supplyAsync(() -> {
            return "b3";
        });

        // allOf当所有任务都执行完毕才会继续执行
        CompletableFuture.allOf(b1, b2, b3).whenComplete((r, e) -> {
            if (e != null) {
                e.printStackTrace();
            }
        });
        System.out.println(b1.get());
        System.out.println(b2.get());
        System.out.println(b3.get());

        CompletableFuture<String> a1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "a1";
        });

        CompletableFuture<String> a2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "a2";
        });

        CompletableFuture<String> a3 = CompletableFuture.supplyAsync(() -> {
            return "a3";
        });

        // anyOf当任一任务执行完毕就会继续执行
        CompletableFuture.anyOf(a1, a2, a3).whenComplete((r, e) -> {
            if (e != null) {
                e.printStackTrace();
            }
            System.out.println(r);
        });

        List<CompletableFuture<String>> futureList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            futureList.add(CompletableFuture.supplyAsync(() -> finalI + ""));
        }
        // 同步获取结果集
        List<String> rs = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                .thenApply(e -> futureList.stream().map(CompletableFuture::join).collect(Collectors.toList())).join();
        System.out.println(rs);
    }

}
