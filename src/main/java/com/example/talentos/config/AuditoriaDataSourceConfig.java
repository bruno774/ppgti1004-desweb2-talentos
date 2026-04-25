package com.example.talentos.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração do DataSource <strong>secundário</strong> — banco de auditoria (Supabase).
 *
 * <ul>
 *   <li>Escaneia entidades em {@code com.example.talentos.model.auditoria}</li>
 *   <li>Escaneia repositórios em {@code com.example.talentos.repository.auditoria}</li>
 *   <li>Operações de auditoria usam {@code auditoriaTransactionManager}</li>
 * </ul>
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.talentos.repository.auditoria",
        entityManagerFactoryRef = "auditoriaEntityManagerFactory",
        transactionManagerRef = "auditoriaTransactionManager"
)
public class AuditoriaDataSourceConfig {

    /**
     * DataSource de auditoria — lê as propriedades {@code spring.datasource.auditoria.*}
     * do {@code application.properties}.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.auditoria")
    public DataSource auditoriaDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * EntityManagerFactory dedicado ao banco de auditoria.
     * Gerencia as entidades do pacote {@code com.example.talentos.model.auditoria}.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean auditoriaEntityManagerFactory(
            @Qualifier("auditoriaDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.talentos.model.auditoria");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        factory.setJpaVendorAdapter(adapter);
        factory.setPersistenceUnitName("auditoria");

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        factory.setJpaPropertyMap(props);

        return factory;
    }

    /**
     * TransactionManager para o banco de auditoria.
     * Deve ser referenciado explicitamente com
     * {@code @Transactional("auditoriaTransactionManager")} nos serviços de auditoria.
     */
    @Bean
    public PlatformTransactionManager auditoriaTransactionManager(
            @Qualifier("auditoriaEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {

        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(factory.getObject());
        return tm;
    }
}
