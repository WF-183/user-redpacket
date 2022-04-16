package com.jindidata.user.redpacket.configuration.strategy;

import com.jindidata.user.redpacket.handler.BaseHandler;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式 逻辑分派定义类
 */
@Data
public class StrategyDispatcher {

    /**
     * 逻辑分派Map
     */
    private Map<String, BaseHandler> dispatcher = new HashMap<>();



    public void putLogicHandler(String key, BaseHandler handler) {
        dispatcher.put(key, handler);
    }

    public BaseHandler getLogicHandler(String key) {
        return dispatcher.get(key);
    }

    public void removeLogicHandler(String key) {
        dispatcher.remove(key);
    }
}
