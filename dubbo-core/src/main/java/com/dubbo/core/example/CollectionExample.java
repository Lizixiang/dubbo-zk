package com.dubbo.core.example;

import com.alibaba.fastjson.JSON;
import com.dubbo.core.example.vo.GoodRankListVO;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lizixiang
 * @since 2022/1/11
 */
public class CollectionExample {

    public static void main(String[] args) {
//        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4);
//        List<Integer> integers = list.subList(2, list.size() + 1);
//        System.out.println(integers);

        ArrayList<Integer> list1 = Lists.newArrayList(22, 1, 2, 25, -1,18, 19);
        ArrayList<Integer> list2 = Lists.newArrayList(1, 2, 18, 25, 19, 22);
        List<Integer> collect = list1.stream().sorted((v1, v2) -> {
            return list2.indexOf(v1) > list2.indexOf(v2) ? 1 : -1;
        }).collect(Collectors.toList());
        System.out.println(collect);

        GoodRankListVO vo1 = new GoodRankListVO(3, 1.1, 3);
        GoodRankListVO vo2 = new GoodRankListVO(2, 1.1, 3);
        List<GoodRankListVO> list = new ArrayList<>();
        list.add(vo1);
        list.add(vo2);
        List<GoodRankListVO> collect1 = list.stream().sorted((b1, b2) -> {
            if (b1.getGrade() > b2.getGrade()) {
                return -1;
            } else if (b1.getGrade() < b2.getGrade()) {
                return 1;
            } else {
                return b2.getGradeCount() - b1.getGradeCount();
            }
        }).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(collect1));

    }

}
