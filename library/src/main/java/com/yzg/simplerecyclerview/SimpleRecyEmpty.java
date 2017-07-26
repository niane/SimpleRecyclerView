package com.yzg.simplerecyclerview;

/**
 * Created by yzg on 2017/3/30.
 */

public interface SimpleRecyEmpty {

    /**
     * 一般错误
     * @param msg
     */
    void onRefreshError(String msg);

    /**
     * 网络连接错误
     * @param msg
     */
    void onNetworkError(String msg);

    /**
     * 数据为空
     * @param msg
     */
    void onEmpty(String msg);

    /**
     * 正在加载
     * @param msg
     */
    void onLoading(String msg);
}
