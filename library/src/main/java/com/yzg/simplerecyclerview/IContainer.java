package com.yzg.simplerecyclerview;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by yzg on 2017/3/30.
 */

interface IContainer {

    void addHeaderView(View view);

    void addHeaderView(@LayoutRes int resLayout);

    void addFooterView(View view);

    void addFooterView(@LayoutRes int resLayout);

    void setLoadMoreView(View view);

    void setLoadMoreView(@LayoutRes int resLayout);

    void setEmptyView(View view);

    void setEmptyView(@LayoutRes int resLayout);

    boolean hasHeader();

    boolean hasFooter();

    boolean hasLoadMore();

    boolean hasEmpty();

    void setStatus(@SimpleRecyclerView.Status int status);

    int getStatus();

    void setOnLoadListener(SimpleRecyclerView.OnLoadListener onLoadMoreListener);
}
