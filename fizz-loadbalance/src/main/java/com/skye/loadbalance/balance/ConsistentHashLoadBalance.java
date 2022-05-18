package com.skye.loadbalance.balance;

import com.skye.loadbalance.AbstractLoadBalance;
import com.skye.loadbalance.Invoker;
import com.skye.loadbalance.LoadBalance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    private final ConcurrentMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<String, ConsistentHashSelector>();

    @Override
    protected Invoker doSelect(List<Invoker> invokers, String param) {
        String serviceKey = invokers.get(0).getServiceKey();
        String url = invokers.get(0).getUrl();
        String key = serviceKey + "." + url;
        int invokersHashCode = invokers.hashCode();
        ConsistentHashSelector selector = selectors.get(key);
        if (selector == null || selector.identityHashCode != invokersHashCode) {
            selectors.put(key, new ConsistentHashSelector(invokers, invokersHashCode));
            selector = selectors.get(key);
        }
        return selector.select(param);
    }

    public final static class ConsistentHashSelector<T> {

        //hash环
        private final TreeMap<Long, Invoker> virtualInvokers;
        //虚拟节点个数
        private final int replicaNumber = 160;
        //hash值,判断invoker发生变化
        private final int identityHashCode;
        //需要参与 Hash 计算的参数索引
        private final int[] argumentIndex;

        ConsistentHashSelector(List<Invoker> invokers, int identityHashCode) {
            this.virtualInvokers = new TreeMap<Long, Invoker>();
            this.identityHashCode = identityHashCode;
            //默认对第一个参数hash
            argumentIndex = new int[]{0};
            for (Invoker invoker : invokers) {
                String address = invoker.getAddress();
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(address + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }
        }

        public Invoker select(String param) {
            String key = toKey(param);
            byte[] digest = md5(key);
            return selectForKey(hash(digest, 0));
        }

        private String toKey(String param) {
            StringBuilder buf = new StringBuilder();
            /*for (int i : argumentIndex) {
                if (i >= 0 && i < args.length) {
                    buf.append(args[i]);
                }
            }*/
            buf.append(param);
            return buf.toString();
        }

        private Invoker selectForKey(long hash) {
            Map.Entry<Long, Invoker> entry = virtualInvokers.ceilingEntry(hash);
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }

        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        private byte[] md5(String value) {
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            md5.reset();
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            md5.update(bytes);
            return md5.digest();
        }
    }

    public static void main(String[] args) {
        Invoker build1 = Invoker.builder().address("192.168.1.1").serviceKey("user").url("/getName").build();
        Invoker build2 = Invoker.builder().address("192.168.1.2").serviceKey("user").url("/getName").build();
        Invoker build3 = Invoker.builder().address("192.168.1.3").serviceKey("user").url("/getName").build();
        List<Invoker> invokers = new ArrayList<>();
        invokers.add(build1);
        invokers.add(build2);
        invokers.add(build3);
        LoadBalance loadBalance = new ConsistentHashLoadBalance();
        Invoker select1 = loadBalance.select(invokers,"{name=1}");
        Invoker select2 = loadBalance.select(invokers,"{name=1}");
        Invoker select3 = loadBalance.select(invokers,"{name=3}");
        System.out.println(1);
    }
}