package com.example.talentos.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Configuração do DataSource <strong>primário</strong> — banco de negócio (Supabase).
 *
 * <ul>
 *   <li>Escaneia entidades em {@code com.example.talentos.model}</li>
 *   <li>Escaneia repositórios em {@code com.example.talentos.repository.negocio}</li>
 *   <li>Todas as operações de negócio usam {@code negocioTransactionManager}</li>
 * </ul>
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.talentos.repository.negocio",
        entityManagerFactoryRef = "negocioEntityManagerFactory",
        transactionManagerRef = "negocioTransactionManager"
)
public class NegocioDataSourceConfig {

    /**
     * DataSource primário — lê as propriedades {@code spring.datasource.negocio.*}
     * do {@code application.properties}.
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.negocio")
    public DataSource negocioDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * EntityManagerFactory dedicado ao banco de negócio.
     * Gerencia as entidades do pacote {@code com.example.talentos.model}.
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean negocioEntityManagerFactory(
            @Qualifier("negocioDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        // Escaneia apenas o pacote de negócio.
        // RegistroAuditoria (subpacote model.auditoria) também é encontrado aqui,
        // mas seus registros só são escritos pelo auditoriaEntityManagerFactory.
        factory.setPackagesToScan("com.example.talentos.model");
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.POSTGRESQL);
        factory.setJpaVendorAdapter(adapter);
        factory.setPersistenceUnitName("negocio");

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        factory.setJpaPropertyMap(props);

        return factory;
    }

    /**
     * TransactionManager para o banco de negócio.
     * Deve ser referenciado como {@code @Transactional("negocioTransactionManager")}
     * nas operações de negócio (ou simplesmente {@code @Transactional} por ser primário).
     */
    @Bean
    @Primary
    public PlatformTransactionManager negocioTransactionManager(
            @Qualifier("negocioEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {

        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(factory.getObject());
        return tm;
    }
}
