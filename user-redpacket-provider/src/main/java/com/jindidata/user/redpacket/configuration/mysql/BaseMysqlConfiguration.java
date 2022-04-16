package com.jindidata.user.redpacket.configuration.mysql;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.net.URL;
import java.nio.file.Paths;

public class BaseMysqlConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMysqlConfiguration.class);

    public SqlSessionFactory createSqlSessionFactory(DataSource source, String xmlPath) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(source);
        Resource[] resource = null;

        String path = Paths.get(xmlPath.substring("classpath:".length())).getParent().toString();
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        URL url = cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path);
        if (url != null) {
            try {
                resource = new PathMatchingResourcePatternResolver().getResources(xmlPath);
            } catch (Exception e) {
                LOGGER.error("cannot find resource from: {}, skip", xmlPath);
            }
        }

        Configuration configuration = new Configuration();
        configuration.setCallSettersOnNulls(true);
        sqlSessionFactoryBean.setConfiguration(configuration);

        sqlSessionFactoryBean.setMapperLocations(resource);
        return sqlSessionFactoryBean.getObject();
    }
}
