/**
 * @(#)UserStatus.java, 2017/4/13.
 */
package com.tigerbrokers.templates.model;

/**
 *
 * @author LIU Shufa
 */
public enum UserStatus {

    ACTIVE(0),
    BLOCKED(1),
    DELETED(2);

    private byte id;

    UserStatus(int id) {
        this.id = (byte)id;
    }

    public byte getId() {
        return id;
    }

    public static UserStatus of(byte id) {
        for (UserStatus status: values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new RuntimeException("Unknown id");
    }
}
