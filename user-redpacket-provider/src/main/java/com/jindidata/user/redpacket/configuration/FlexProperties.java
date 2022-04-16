package com.jindidata.user.redpacket.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "user.flex")
public class FlexProperties {

    /**
     * 红包超时自动退回
     */
    private Integer redPacketTimeoutSec;

}
