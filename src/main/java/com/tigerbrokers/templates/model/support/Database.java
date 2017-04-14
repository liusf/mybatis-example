/**
 * @(#)Database.java, 16/1/22.
 */
package com.tigerbrokers.templates.model.support;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 *
 * @author LIU Shufa
 */
public class Database {

    private static final String CONFIG_FILE = "mybatis-config.xml";

    private final String name;

    private final SqlSessionFactory sessionFactory;

    private final SqlSessionTemplate session;

    private final DataSource dataSource;

    public Database(String name) {
        this.name = name;
        try (Reader reader = Resources.getResourceAsReader(CONFIG_FILE)) {
            Properties properties = properties(name);
            SqlSessionFactory tmpSessionFactory = new SqlSessionFactoryBuilder().build(reader, properties);
            dataSource = tmpSessionFactory.getConfiguration().getEnvironment().getDataSource();
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(CONFIG_FILE));
            sessionFactory = sqlSessionFactoryBean.getObject();
            session = new SqlSessionTemplate(sessionFactory);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 从 database.${name} 中获取数据库配置
     * @param name
     * @return
     */
    private Properties properties(String name) {
        Properties properties = new Properties();
        Config config = ConfigFactory.load();
        String path = "database." + name;
        if (!config.hasPath(path)) {
            return null;
        }
        Config dbConfig = config.getConfig(path);
        properties.put("database.driver", dbConfig.getString("driver"));
        properties.put("database.url", dbConfig.getString("url"));
        properties.put("database.username", dbConfig.getString("username"));
        properties.put("database.password", dbConfig.getString("password"));
        return properties;
    }

    public String getName() {
        return this.name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public SqlSessionTemplate getSession() {
        return session;
    }
}
