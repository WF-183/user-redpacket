package com.jindidata.user.redpacket.configuration.strategy;

import com.jindidata.user.redpacket.constant.Global;
import com.jindidata.user.redpacket.handler.AvgDivHandler;
import com.jindidata.user.redpacket.handler.LineDivHandler;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {



    private final LineDivHandler lineDivHandler;
    private final AvgDivHandler avgDivHandler;

    public HandlerConfiguration(LineDivHandler lineDivHandler, AvgDivHandler avgDivHandler) {
        this.lineDivHandler = lineDivHandler;
        this.avgDivHandler = avgDivHandler;
    }

    @RefreshScope
    @Bean
    public StrategyDispatcher strategyDispatcher() {
        StrategyDispatcher strategyDispatcher = new StrategyDispatcher();
        //具体策略
        strategyDispatcher.putLogicHandler(Global.RED_TYPE_01, lineDivHandler);
        strategyDispatcher.putLogicHandler(Global.RED_TYPE_02, avgDivHandler);
        return strategyDispatcher;
    }

}
