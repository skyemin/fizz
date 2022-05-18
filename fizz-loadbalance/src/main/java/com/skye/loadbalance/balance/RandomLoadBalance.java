package com.skye.loadbalance.balance;

import com.skye.loadbalance.AbstractLoadBalance;
import com.skye.loadbalance.Invoker;
import com.skye.loadbalance.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected Invoker doSelect(List<Invoker> invokers, String param) {

        int length = invokers.size();
        boolean sameWeight = true;
        int[] weights = new int[length];
        // 计算第一个Invoker的权重
        int firstWeight = invokers.get(0).getWeight();
        weights[0] = firstWeight;
        int totalWeight = firstWeight;
        for (int i = 1; i < length; i++) {
            // 计算每个Invoker的权重，以及总权重totalWeight
            int weight = invokers.get(i).getWeight();
            weights[i] = weight;
            totalWeight += weight;
            //判断权重是否相同
            if (sameWeight && weight != firstWeight) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // 随机获取一个[0, totalWeight) 区间内的数字
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            // 循环让offset数减去Invoker的权重值，当offset小于0时，返回相应的Invoker
            for (int i = 0; i < length; i++) {
                offset -= weights[i];
                if (offset < 0) {
                    return invokers.get(i);
                }
            }
        }
        //权重相同随机返回一个
        return invokers.get(ThreadLocalRandom.current().nextInt(length));
    }

    public static void main(String[] args) {
        Invoker build1 = Invoker.builder().address("192.168.1.1").serviceKey("user").url("/getName").weight(1).build();
        Invoker build2 = Invoker.builder().address("192.168.1.2").serviceKey("user").url("/getName").weight(3).build();
        Invoker build3 = Invoker.builder().address("192.168.1.3").serviceKey("user").url("/getName").weight(6).build();
        List<Invoker> invokers = new ArrayList<>();
        invokers.add(build1);
        invokers.add(build2);
        invokers.add(build3);
        LoadBalance loadBalance = new RandomLoadBalance();
        Invoker select1 = loadBalance.select(invokers,null);
        Invoker select2 = loadBalance.select(invokers,null);
        Invoker select3 = loadBalance.select(invokers,null);
        System.out.println(1);
    }
}
