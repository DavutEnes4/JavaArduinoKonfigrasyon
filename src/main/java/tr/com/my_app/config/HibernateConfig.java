package tr.com.my_app.config;

import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan("tr.com.my_app")
public class HibernateConfig {

    /**
     * Slaytlardaki tüm hibernate + c3p0 + mysql ayarlarını bu bean ile sağlıyoruz.
     */
    @Bean("hibernateProps")
    public Properties hibernateProperties() throws IOException {
        return PropertiesLoaderUtils
                .loadProperties(new ClassPathResource("hibernate.properties"));
    }

    /**
     * c3p0 havuzlu DataSource
     */
    @Bean
    public DataSource dataSource(
            @Qualifier("hibernateProps") Properties props
    ) throws Exception {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass( props.getProperty("mysql.driver") );
        ds.setJdbcUrl(     props.getProperty("mysql.url") );
        ds.setUser(        props.getProperty("mysql.user") );
        ds.setPassword(    props.getProperty("mysql.password") );
        // c3p0 ayarları
        ds.setMinPoolSize(           Integer.parseInt(props.getProperty("hibernate.c3p0.min_size")) );
        ds.setMaxPoolSize(           Integer.parseInt(props.getProperty("hibernate.c3p0.max_size")) );
        ds.setAcquireIncrement(      Integer.parseInt(props.getProperty("hibernate.c3p0.acquire_increment")) );
        ds.setMaxIdleTime(           Integer.parseInt(props.getProperty("hibernate.c3p0.timeout")) );
        ds.setMaxStatements(         Integer.parseInt(props.getProperty("hibernate.c3p0.max_statements")) );
        ds.setIdleConnectionTestPeriod(
                Integer.parseInt(props.getProperty("hibernate.c3p0.idle_test_period")) );
        ds.setAcquireRetryAttempts(  Integer.parseInt(props.getProperty("hibernate.c3p0.acquireRetryAttempts")) );
        ds.setAcquireRetryDelay(     Integer.parseInt(props.getProperty("hibernate.c3p0.acquireRetryDelay")) );
        return ds;
    }

    /**
     * LocalSessionFactoryBean tanımı. Bean adı 'sessionFactory' olarak kaydediliyor.
     */
    @Bean("sessionFactory")
    public LocalSessionFactoryBean sessionFactory(
            DataSource dataSource,
            @Qualifier("hibernateProps") Properties props
    ) {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource);
        sf.setPackagesToScan("tr.com.my_app.model");
        sf.setHibernateProperties(props);
        return sf;
    }

    /**
     * PlatformTransactionManager için, FactoryBean'den üretilen SessionFactory'i inject ediyoruz.
     */
    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("sessionFactory") SessionFactory sessionFactory
    ) {
        return new HibernateTransactionManager(sessionFactory);
    }
}
