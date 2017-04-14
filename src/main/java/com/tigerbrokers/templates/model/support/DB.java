/**
 * @(#)DB.java, 16/1/22.
 */
package com.tigerbrokers.templates.model.support;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author LIU Shufa
 */
public abstract class DB {

    private static final ConcurrentHashMap<String, Database> DATABASES = new ConcurrentHashMap<String, Database>();

    public static Database db() {
        return db("default");
    }

    public static Database inMemory() {
        return db("h2");
    }

    public static Database db(String name) {
        return DATABASES.computeIfAbsent(name, n -> new Database(n));
    }
}
