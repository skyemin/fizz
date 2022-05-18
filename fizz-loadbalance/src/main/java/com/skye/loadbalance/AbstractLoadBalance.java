package com.skye.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance{

    public Invoker select(List<Invoker> invokers,String param){
        if (invokers.size() == 0) {
            return null;
        }
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        return doSelect(invokers,param);
    }

    protected abstract Invoker doSelect(List<Invoker> invokers,String param);
}
