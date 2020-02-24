package com.fy.real.min.weibo.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * [Create]
 * Description:
 * <br/>Date: 2020/2/22 20:53 - Create
 *
 * @author fengyu.zhang
 * @version 1.0
 */
@Configuration
public class DbConfig {

    private static final String MAPPER_LOCATION = "classpath:mapper/*.xml";
    private static final String MODEL_PACKAGE = "com.fy.real.min.weibo.model.entity";

    @Value("${ds1.datasource.url}")
    private String url;
    @Value("${ds1.datasource.username}")
    private String username;
    @Value("${ds1.datasource.password}")
    private String password;
    @Value("${ds1.datasource.driverClassName}")
    private String driverClassName;

    @Value("${ds1.datasource.maxActive}")
    private Integer maxActive;
    @Value("${ds1.datasource.minIdle}")
    private Integer minIdle;
    @Value("${ds1.datasource.initialSize}")
    private Integer initialSize;
    @Value("${ds1.datasource.maxWait}")
    private Long maxWait;
    @Value("${ds1.datasource.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${ds1.datasource.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${ds1.datasource.testOnBorrow}")
    private Boolean testOnReturn;

    @Bean(name = "druidDataSource")
    public DruidDataSource initDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory hkOrderSqlSessionFactory(@Qualifier("druidDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(DbConfig.MODEL_PACKAGE);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DbConfig.MAPPER_LOCATION));

        //分页插件
        Interceptor interceptor = new PageInterceptor();
        Properties properties = new Properties();
        //数据库
        properties.setProperty("helperDialect", "mysql");
        //是否将参数offset作为PageNum使用
        properties.setProperty("offsetAsPageNum", "true");
        //是否进行count查询
        properties.setProperty("rowBoundsWithCount", "true");
        //是否分页合理化
        properties.setProperty("reasonable", "true");
        interceptor.setProperties(properties);
        bean.setPlugins(interceptor);

        return bean.getObject();
    }
}
