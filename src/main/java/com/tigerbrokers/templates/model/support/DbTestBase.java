/**
 * @(#)DbTestBase.java, 16/1/25.
 */
package com.tigerbrokers.templates.model.support;

import org.apache.ibatis.migration.DataSourceConnectionProvider;
import org.apache.ibatis.migration.FileMigrationLoader;
import org.apache.ibatis.migration.operations.BootstrapOperation;
import org.apache.ibatis.migration.operations.DownOperation;
import org.apache.ibatis.migration.operations.UpOperation;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mybatis.spring.SqlSessionTemplate;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *  使用H2内存数据库进行DAO方法的单元测试. testcase应该继承本类,并注入本类的{@link #sqlSession()} 到DAO中.
 *  本类会使用 mybatis migrations 下的脚本在H2中创建数据库的表结构和初始数据,从而和线上数据库保持一致.
 *
 * @author LIU Shufa
 */
public abstract class DbTestBase {

    private static class InstanceHolder {
        static final Database database = DB.inMemory();
    }

    protected static Database database() {
        return InstanceHolder.database;
    }

    protected static SqlSessionTemplate sqlSession() {
        return database().getSession();
    }

    private static String MIGRATION_SCRIPTS_FOLDER = "./migrations/scripts";

    @BeforeClass
    public static void createTables() {
        DataSourceConnectionProvider connectionProvider = getDataSourceConnectionProvider();
        FileMigrationLoader migrationsLoader = getFileMigrationLoader();
        new BootstrapOperation().operate(connectionProvider, migrationsLoader, null, null);
        new UpOperation().operate(connectionProvider, migrationsLoader, null, null);
    }

    private static FileMigrationLoader getFileMigrationLoader() {
        return new FileMigrationLoader(new File(MIGRATION_SCRIPTS_FOLDER), "UTF-8", null);
    }

    private static DataSourceConnectionProvider getDataSourceConnectionProvider() {
        return new DataSourceConnectionProvider(database().getDataSource());
    }

    @AfterClass
    public static void dropTables() {
        new DownOperation().operate(getDataSourceConnectionProvider(), getFileMigrationLoader(), null, null);
    }

    /**
     * 可以Override该方法指定初始测试数据文件. 文件
     * @return
     */
    public String fixtureFileName() {
        return null;
    }

    @Before
    public void setUpDb() throws Exception {
        cleanAllTables();
        // TODO set up fixtures

    }

    private void cleanAllTables() throws SQLException {
        try (Connection connection = database().getDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resutlSet = statement.executeQuery("show tables");
            List<String> tableNames = new ArrayList<>();
            while (resutlSet.next()) {
                String tableName = resutlSet.getString(1);
                if (tableName != "changelog") {
                    tableNames.add(tableName);
                }
            }
            tableNames.stream().forEach(tableName -> {
                try {
                    statement.execute(String.format("DELETE FROM %s", tableName));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
