package com.supermap.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.supermap.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

/**
 * DruidDataSource ,数据库连接池配置类
 *
 * @author leishiguang
 * @version v1.0.0
 * @since v1.0
 */
@Slf4j
@Configuration
public class DruidDataSourceConfig {

    @Value("${ops.work.bdck.database.url}")
    private String bdckDatabaseUrl;
    @Value("${ops.work.bdck.database.password}")
    private String bdckDatabasePassword;
    @Value("${spring.profiles.active}")
    private String activeEnvironment;


    @Bean(name = "masterDataSource")
    @Qualifier("masterDataSource")
    public DruidDataSource masterDataSource(){
        //获取项目根目录
        String rootPath = new FileSystemResource("db/h2").getFile().getAbsolutePath();
        String url;
        switch (activeEnvironment){
            case "product":{
                url = "jdbc:h2:file:" + rootPath + "/database-product";
                break;
            }
            case "dev":{
                url = "jdbc:h2:file:" + rootPath + "/database-dev";
                break;
            }
            default:{
                url = "jdbc:h2:file:" + rootPath + "/database-test";
            }
        }
        String driverClass = "org.h2.Driver";
        String username = "opsmaster";
        String password = "masterdatasource";
        DruidDataSource druidDataSource = this.initDefaultDruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName(driverClass);
        druidDataSource.setName("masterDataSource");
        return druidDataSource;
    }

    @Bean(name = "bdckDataSource")
    @Qualifier("bdckDataSource")
    public DruidDataSource bdckDataSource() {
        String url = "jdbc:oracle:thin:@" + bdckDatabaseUrl;
        DruidDataSource druidDataSource = this.initDefaultDruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername("bdck");
        druidDataSource.setPassword(bdckDatabasePassword);
        druidDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        druidDataSource.setName(StringUtil.generateDbName("bdck", url));
        return druidDataSource;
    }

    /**
     * 生成默认的数据源配置
     */
    private DruidDataSource initDefaultDruidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        //连接池初始化连接数量
        druidDataSource.setInitialSize(5);
        //连接池最大活跃连接数
        druidDataSource.setMaxActive(100);
        //最小空闲数
        druidDataSource.setMinIdle(5);
        //最大等待时间
        druidDataSource.setMaxWait(60000);
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        //配置一个连接在池中最小生存的时间，单位是毫秒
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        //连接是否有效的查询语句
        druidDataSource.setValidationQuery("SELECT 1 FROM DUAL");
        druidDataSource.setTestOnBorrow(true);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(false);
        //打开PSCache，并且指定每个连接上PSCache的大小
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(50);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setUseGlobalDataSourceStat(true);
        try {
            //配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
            druidDataSource.setFilters("stat,wall");
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }
        return druidDataSource;
    }



}
