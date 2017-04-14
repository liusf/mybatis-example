/**
 * @(#)DaoConfiguration.java, 16/5/13.
 */
package com.tigerbrokers.templates.model.dao;

import com.tigerbrokers.templates.model.support.DB;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * spring DAO相关的Bean定义. 使用DAO类的spring程序中,需要把这个Configuration类Import进去.
 * <pre class="code">
 *     &#064;SpringBootApplication(scanBasePackages = "com.tigerbrokers.example")
 *     &#064;EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
 *     &#064;Import({DaoConfiguration.class})
 *     public class Application {
 *
 *         public static void main(String[] args) {
 *             SpringApplication.run(Application.class, args);
 *         }
 *     }
 *
 * </pre>
 * @author LIU Shufa
 */
@Configuration
public class DaoConfiguration {

    @Bean(name = "sqlSession")
    public SqlSessionTemplate sqlSession() {
        return DB.db().getSession();
    }

    /**
     * 多个db使用不同的 sqlSession
     **/
    @Bean(name = "authSqlSession")
    public SqlSessionTemplate authSqlSession() {
        return DB.db("auth").getSession();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        return DB.db().getSessionFactory();
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(DB.db().getDataSource());
    }

    /**
     * 默认数据库上的 TransactionTemplate. 多个数据库情况下,每个数据库都需要定义一个
     * @return
     */
    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(txManager());
    }
}
