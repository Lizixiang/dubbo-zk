package com.dubbo.core.example;

import com.dubbo.core.util.DateUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author lizixiang
 * @since 2021/11/5
 */
public class DurationAndPeriodExample {

    public static void main(String[] args) {

        ///////////////////////////////////////Period使用///////////////////////////////////////

        // Period的between获取的都是当年|当月|当日的差（基本不用）
        LocalDate now = LocalDate.now();
        LocalDate localDate = LocalDate.of(2020, 9, 1);
        Period between = Period.between(localDate, now);
        System.out.println("相差"+ between.getYears() +"年|"+ between.getMonths() +"月|"+ between.getDays() +"日");

        // 加减运算 plus、minus
        Period.of(3, 10, 10);
        Period p1 = Period.parse("P3Y5M4D");
        System.out.println(p1.plus(Period.of(1, 1, 1)));
        System.out.println(p1.plusDays(3));
        System.out.println(p1.plusMonths(1));
        System.out.println(p1.plusYears(1));
        // 进行plus操作并不会对p1本身有影响
        System.out.println(p1);

        ///////////////////////////////////////Duration使用///////////////////////////////////////
        // Duration的between获取的是秒|纳秒
        LocalDateTime begin = LocalDateTime.of(2020, 9, 5, 14, 53, 0);
        LocalDateTime end = LocalDateTime.now();
        Duration between1 = Duration.between(begin, end);
        System.out.println("相差"+ between1.getSeconds() +"s|"+ between1.getNano() +"ns");

        System.out.println(Duration.ofHours(10));
        // 将时间转换成时间单元
        System.out.println(Duration.parse("P1DT1H10M43.2S").toHours());
        System.out.println(Duration.parse("PT23H").toMinutes());

        Duration duration = Duration.ofSeconds(10);
        System.out.println(duration.plus(Duration.parse("P1DT1H")));
        System.out.println(duration.plusDays(1));
        System.out.println(duration.minusHours(2));
        // 进行plus操作并不会对p1本身有影响
        System.out.println(duration);
        System.out.println(DateUtils.formatDurationWords(260000, true, true));


        ///////////////////////////////////////ChronoUnit使用///////////////////////////////////////
        // ChronoUnit可以获取年月周日时分秒等时间差
        System.out.println("相差"+ ChronoUnit.YEARS.between(begin, end) +"年");
        System.out.println("相差"+ ChronoUnit.MONTHS.between(begin, end) +"个月");
        System.out.println("相差"+ ChronoUnit.WEEKS.between(begin, end) +"周");
        System.out.println("相差"+ ChronoUnit.DAYS.between(begin, end) +"天(24小时制)");
        System.out.println("相差"+ ChronoUnit.HALF_DAYS.between(begin, end) +"天(12小时制)");
        System.out.println("相差"+ ChronoUnit.HOURS.between(begin, end) +"小时");
        System.out.println("相差"+ ChronoUnit.MINUTES.between(begin, end) +"分钟");
        System.out.println("相差"+ ChronoUnit.SECONDS.between(begin, end) +"秒");
        System.out.println("相差"+ ChronoUnit.MILLIS.between(begin, end) +"mesc");
        System.out.println("相差"+ ChronoUnit.MICROS.between(begin, end) +"mics");
        System.out.println("相差"+ ChronoUnit.NANOS.between(begin, end) +"ns");

        ///////////////////////////////////////Instant使用///////////////////////////////////////
        Instant instant = Instant.now();
        System.out.println(instant);
        System.out.println(Instant.now(Clock.systemDefaultZone()));
        System.out.println(Instant.ofEpochMilli(1636630631000L));
        System.out.println(Instant.ofEpochSecond(1636630631));
        System.out.println(Instant.parse("2021-11-11T19:38:35Z"));
        System.out.println(instant.toEpochMilli());// ms
        System.out.println(instant.getEpochSecond());
    }

}
