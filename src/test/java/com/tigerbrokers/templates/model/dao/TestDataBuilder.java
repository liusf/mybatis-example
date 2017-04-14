/**
 * @(#)TestDataBuilder.java, 2017/4/14.
 */
package com.tigerbrokers.templates.model.dao;

import com.tigerbrokers.templates.model.UserStatus;
import com.tigerbrokers.templates.model.tables.generated.User;

/**
 *
 * @author LIU Shufa
 */
public abstract class TestDataBuilder {

    public static User newUser() {
        User user = new User();
        user.setName("Bob");
        user.setAge(25);
        user.setStatus(UserStatus.ACTIVE.getId());
        return user;
    }
}
