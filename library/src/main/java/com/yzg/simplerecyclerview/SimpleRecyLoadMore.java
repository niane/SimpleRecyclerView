package com.yzg.simplerecyclerview;

/**
 * Created by yzg on 2017/3/30.
 */

public interface SimpleRecyLoadMore {
    /**
     * 正在加载
     * @param msg
     */
    void onLoading(String msg);

    /**
     * 数据已全部加载
     * @param msg
     */
    void onLoadOver(String msg);

    /**
     * 加载出错
     * @param msg
     */
    void onLoadError(String msg);
}
