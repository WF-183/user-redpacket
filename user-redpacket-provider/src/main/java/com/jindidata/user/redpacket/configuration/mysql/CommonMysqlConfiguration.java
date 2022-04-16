package com.jindidata.user.redpacket.configuration.mysql;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CommonMysqlConfiguration {

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConfigurationProperties(prefix = "datasource.common")
    @Bean("commonDSC")
    public HikariConfig commonDataSourceConfig() {
        return new HikariConfig();
    }
}
