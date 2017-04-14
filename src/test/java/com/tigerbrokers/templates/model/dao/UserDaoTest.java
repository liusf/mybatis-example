/**
 * @(#)UserDaoTest.java, 2017/4/14.
 */
package com.tigerbrokers.templates.model.dao;

import com.tigerbrokers.templates.model.UserStatus;
import com.tigerbrokers.templates.model.dao.impl.UserDaoImpl;
import com.tigerbrokers.templates.model.support.DbTestBase;
import com.tigerbrokers.templates.model.tables.generated.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 *
 * @author LIU Shufa
 */
public class UserDaoTest extends DbTestBase {

    UserDao userDao;

    @Before
    public void setUp() {
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        userDaoImpl.setSession(sqlSession());
        userDao = userDaoImpl;
    }

    @Test
    public void testInsert() {
        User user = TestDataBuilder.newUser();
        userDao.insert(user);
        Assert.assertTrue("user id should be positive", user.getId() > 0);
        List<User> activeUsers = userDao.getByStatus(UserStatus.ACTIVE);
        Assert.assertEquals(1, activeUsers.size());
    }


}
