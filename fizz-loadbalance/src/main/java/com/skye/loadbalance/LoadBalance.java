package com.skye.loadbalance;

import java.util.List;

public interface LoadBalance {

    Invoker select(List<Invoker> invokers,String param);

}
