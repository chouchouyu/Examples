package com.github.chouchouyu.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by susan on 2018/6/18.
 */

public class FieldInfo {

    public String name;
    public Field field;
    public Method method;
    public Class type;

    public FieldInfo(String name, Method method, Field field) {
        this.name = name;
        this.field = field;
        this.method = method;
        type = method != null ? method.getReturnType() : field.getType();
    }

    public FieldInfo(String name, Method method, Field field, Class type) {
        this.name = name;
        this.field = field;
        this.method = method;
        this.type = type;
    }

    public Object get(Object object) {
        try {
            return method != null ? method.invoke(object) : field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void set(Object object, Object vlaue) {
        try {
            if (method != null) {
                method.invoke(object, vlaue);
            } else {
                field.set(object, vlaue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
