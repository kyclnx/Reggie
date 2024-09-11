package com.itheima.reggie.common;

public abstract class BaseContext{
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentIt(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
