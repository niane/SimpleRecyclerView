package com.yzg.simplerecyclerview;

/**
 * Created by yzg on 2017/3/30.
 */

public interface SimpleRecyLoadMore {
    void onLoading(String msg);
    void onLoadOver(String msg);
    void onLoadError(String msg);
}
