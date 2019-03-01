package com.example.waste_management_server;

import com.netflix.config.DynamicPropertyFactory;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.waste_management_server",
    entityManagerFactoryRef = "coreEntityManager",
    transactionManagerRef = "coreTransactionManager"
)
@Slf4j
public class CorePersistenceConfig {

    @Value("${persistence.user:#{null}}") private String username;
    @Value("${persistence.password:#{null}}") private String password;
    @Value("${persistence.databaseName:#{null}}") private String database;
    @Value("${persistence.serverName:#{null}}") private String server;
    @Value("${persistence.portNumber:#{null}}") private Integer port;
    @Value("${persistence.maximumPoolSize:#{null}}") private Integer maxPoolSize;
    @Value("${persistence.connectionTimeout:#{null}}") private Long connectionTimeout;
    @Value("${persistence.jdbcUrl:#{null}}") private String jdbcUrl;


    private static final String SHOW_SQL = "hibernate.show_sql";
    private static final String FORMAT_SQL = "hibernate.format_sql";

    private int getMaxPoolSize() {
        if(this.maxPoolSize == null)
            this.maxPoolSize = DynamicPropertyFactory.getInstance().getIntProperty("com.example.waste_management_server.persistence.maximumPoolSize", 5).getValue();
        log.debug("maxPoolSize: {}", maxPoolSize);
        return maxPoolSize;
    }

    private String getUsername() {
        String username = this.username;
        if(!StringUtils.hasText(username))
            username = DynamicPropertyFactory.getInstance().getStringProperty("com.example.waste_management_server.persistence.user", "ninjacart").getValue();
        return username;
    }

    private String getPassword() {
        String password = this.password;
        if(!StringUtils.hasText(password))
            password = DynamicPropertyFactory.getInstance().getStringProperty("com.example.waste_management_server.persistence.password", "ninjacart").getValue();
        return password;
    }

    private long getConnectionTimeout() {
        if(this.connectionTimeout == null)
            this.connectionTimeout = DynamicPropertyFactory.getInstance().getLongProperty("com.example.waste_management_server.persistence.connectionTimeout", 5000).getValue();
        log.debug("connectionTimeout: {}", connectionTimeout);
        return connectionTimeout;
    }

    private String getDatabaseName() {
        if(!StringUtils.hasText(this.database))
            this.database = DynamicPropertyFactory.getInstance().getStringProperty("com.example.waste_management_server.persistence.databaseName", "test").getValue();
        return this.database;
    }

    private String getServername() {
        if(!StringUtils.hasText(this.server))
            this.server = DynamicPropertyFactory.getInstance().getStringProperty("com.example.waste_management_server.persistence.serverName", "localhost").getValue();
        log.debug("server: {}", this.server);
        return this.server;
    }

    private int getPortNumber() {
        if(this.port == null)
            this.port = DynamicPropertyFactory.getInstance().getIntProperty("com.example.waste_management_server.persistence.portNumber", 6603).getValue();
        log.debug("port: {}", this.port);
        return this.port;
    }


    private String getJdbcUrl() {
        if(StringUtils.isEmpty(this.jdbcUrl))
            this.jdbcUrl = DynamicPropertyFactory.getInstance().getStringProperty("com.example.waste_management_server.persistence.jdbcUrl", "jdbc:mysql://localhost:6603/test").getValue();
        log.debug("port: {}", this.port);
        return this.jdbcUrl;
    }

    private Boolean getShowSql() {
        return DynamicPropertyFactory.getInstance().getBooleanProperty(SHOW_SQL, true).getValue();
    }

    private Boolean getFormatSql() {
        return DynamicPropertyFactory.getInstance().getBooleanProperty(FORMAT_SQL, true).getValue();
    }

    @Primary
    @Bean
    public javax.sql.DataSource ds1DataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.addDataSourceProperty("databaseName", getDatabaseName());
        ds.addDataSourceProperty("serverName", getServername());
        ds.addDataSourceProperty("portNumber", getPortNumber());
        ds.setJdbcUrl(getJdbcUrl());
        ds.setUsername(getUsername());
        ds.setPassword(getPassword());
        ds.setConnectionTimeout(getConnectionTimeout());
        ds.setMaximumPoolSize(getMaxPoolSize());
        return ds;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean coreEntityManager() {
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds1DataSource());
        em.setPackagesToScan(
                "com.example.waste_management_server");
        HibernateJpaVendorAdapter vendorAdapter
          = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(SHOW_SQL,getShowSql());
        properties.put(FORMAT_SQL,getFormatSql());
        em.setJpaPropertyMap(properties);
        return em;
    }
 

    @Primary
    @Bean
    public PlatformTransactionManager coreTransactionManager() {
        JpaTransactionManager transactionManager
          = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                coreEntityManager().getObject());
        return transactionManager;
    }

}