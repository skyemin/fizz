package com.skye.loadbalance.balance;

import com.skye.loadbalance.AbstractLoadBalance;
import com.skye.loadbalance.Invoker;
import com.skye.loadbalance.LoadBalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private ConcurrentMap<String, ConcurrentMap<String, WeightedRoundRobin>> methodWeightMap = new ConcurrentHashMap<String, ConcurrentMap<String, WeightedRoundRobin>>();
    private static final int RECYCLE_PERIOD = 60000;
    protected static class WeightedRoundRobin {
        private int weight;
        private AtomicLong current = new AtomicLong(0);
        private long lastUpdate;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }
    @Override
    protected Invoker doSelect(List<Invoker> invokers, String param) {
        String serviceKey = invokers.get(0).getServiceKey();
        String url = invokers.get(0).getUrl();
        String key = serviceKey + "." + url;
        ConcurrentMap<String, WeightedRoundRobin> map = methodWeightMap.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = System.currentTimeMillis();
        Invoker selectedInvoker = null;
        WeightedRoundRobin selectedWRR = null;
        for (Invoker invoker : invokers) {
            String identifyString = invoker.getAddress();
            int weight = invoker.getWeight();
            WeightedRoundRobin weightedRoundRobin = map.computeIfAbsent(identifyString, k -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(weight);
                return wrr;
            });

            if (weight != weightedRoundRobin.getWeight()) {
                //weight changed
                weightedRoundRobin.setWeight(weight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(now);
            if (cur > maxCurrent) {
                maxCurrent = cur;
                selectedInvoker = invoker;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;
        }
        if (invokers.size() != map.size()) {
            map.entrySet().removeIf(item -> now - item.getValue().getLastUpdate() > RECYCLE_PERIOD);
        }
        if (selectedInvoker != null) {
            selectedWRR.sel(totalWeight);
            return selectedInvoker;
        }
        // should not happen here
        return invokers.get(0);
    }

    public static void main(String[] args) {
        Invoker build1 = Invoker.builder().address("192.168.1.1").serviceKey("user").url("/getName").weight(5).build();
        Invoker build2 = Invoker.builder().address("192.168.1.2").serviceKey("user").url("/getName").weight(1).build();
        Invoker build3 = Invoker.builder().address("192.168.1.3").serviceKey("user").url("/getName").weight(1).build();
        List<Invoker> invokers = new ArrayList<>();
        invokers.add(build1);
        invokers.add(build2);
        invokers.add(build3);
        LoadBalance loadBalance = new RoundRobinLoadBalance();
        Invoker select1 = loadBalance.select(invokers,null);
        Invoker select2 = loadBalance.select(invokers,null);
        Invoker select3 = loadBalance.select(invokers,null);
        System.out.println(1);
    }
}
