/**
 * @(#)UserDaoImpl.java, 2017/4/13.
 */
package com.tigerbrokers.templates.model.dao.impl;

import com.tigerbrokers.templates.model.UserStatus;
import com.tigerbrokers.templates.model.dao.DaoBase;
import com.tigerbrokers.templates.model.dao.UserDao;
import com.tigerbrokers.templates.model.tables.generated.User;
import com.tigerbrokers.templates.model.tables.generated.UserExample;
import com.tigerbrokers.templates.model.tables.generated.UserMapper;

import java.util.List;

/**
 *
 * @author LIU Shufa
 */
public class UserDaoImpl extends DaoBase implements UserDao {

    public List<User> getByStatus(UserStatus status) {
        UserExample example = new UserExample();
        example.setOrderByClause("id desc");
        example.createCriteria().andStatusEqualTo(status.getId());
        return userMapper().selectByExample(example);
    }

    @Override
    public void insert(User user) {
        userMapper().insert(user);
    }

    @Override
    public boolean updateInTx(Long id) {
        return transactionTemplate.execute(transactionStatus -> {
            try {
                // some operations in transaction

                return true;
            } catch (Exception e) {
                // rollback if throw Exception
                transactionStatus.setRollbackOnly();
                return false;
            }
        });
    }

    UserMapper userMapper() {
        return mapper(UserMapper.class);
    }
}
