/**
 * @(#)DaoBase.java, 16/5/13.
 */
package com.tigerbrokers.templates.model.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author LIU Shufa
 */
public abstract class DaoBase {

    @Autowired
    @Qualifier("sqlSession")
    protected SqlSession session;

    @Autowired
    @Qualifier("authSqlSession")
    protected SqlSession authSession;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    protected <T> T mapper(Class<T> clazz) {
        return session.getMapper(clazz);
    }

    public void setSession(SqlSession session) {
        this.session = session;
    }

    protected static <T> T firstOrNull(List<T> list) {
        return list.size() > 0 ? list.get(0) : null;
    }
}
