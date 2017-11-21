package com.supcoder.fundcrawler.utils;

/**
 * Author：hulei on 2017/11/21 09:03
 * Email：710452831@qq.com
 * Function ：
 */

public class NoDoubleClickUtils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 1000;

    public synchronized static boolean isDoubleClick(){
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        isClick2 = currentTime-lastClickTime<=SPACE_TIME;
        lastClickTime = currentTime;
        return isClick2;
    }

}
