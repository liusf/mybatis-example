/**
 * @(#)UserDao.java, 2017/4/13.
 */
package com.tigerbrokers.templates.model.dao;

import com.tigerbrokers.templates.model.UserStatus;
import com.tigerbrokers.templates.model.tables.generated.User;

import java.util.List;

/**
 *
 * @author LIU Shufa
 */
public interface UserDao {

    List<User> getByStatus(UserStatus status);

    void insert(User user);

    boolean updateInTx(Long id);
}
