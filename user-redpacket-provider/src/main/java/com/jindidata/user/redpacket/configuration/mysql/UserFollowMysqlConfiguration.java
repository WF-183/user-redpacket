package com.jindidata.user.redpacket.configuration.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(SqlSessionTemplate.class)
@ConditionalOnProperty(prefix = "datasource.userfollow", name = {"url", "username", "password"})
@MapperScan(basePackages = "com.jindidata.**.mapper.userfollow", sqlSessionTemplateRef = "userfollowSST")
@Import(CommonMysqlConfiguration.class)
public class UserFollowMysqlConfiguration extends BaseMysqlConfiguration {
    private static final String XML_FILE_PATH = "classpath:mapper/userfollow/*.xml";

    @Bean(name = "userfollowDSC")
    @ConfigurationProperties(prefix = "datasource.userfollow")
    @RefreshScope
    public HikariConfig meDataSourceConfig(@Qualifier("commonDSC") HikariConfig commonDSC) {
        return commonDSC;
    }

    @Bean(name = "userfollowDS")
    @RefreshScope
    public DataSource meDataSource(@Qualifier("userfollowDSC") HikariConfig meDSC) {
        return new HikariDataSource(meDSC);
    }

    @Bean(name = "userfollowSSF")
    @ConfigurationProperties(prefix = "mybatis.userfollow")
    public SqlSessionFactory meSqlSessionFactory(@Qualifier("userfollowDS") DataSource meDS) throws Exception {
        return createSqlSessionFactory(meDS, XML_FILE_PATH);
    }

    @Bean(name = "userfollowSST")
    public SqlSessionTemplate meSqlSessionTemplate(@Qualifier("userfollowSSF") SqlSessionFactory meSSF) {
        return new SqlSessionTemplate(meSSF);
    }

    @Bean(name = "userfollowJdbc")
    public JdbcTemplate meJdbcTemplate(@Qualifier("userfollowDS") DataSource meDS) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(meDS);
        return jdbcTemplate;
    }
}
