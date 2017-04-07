package com.yzg.simplerecyclerview.adapter;

/**
 * Created by yzg on 2017/3/31.
 */

public interface ItemViewDelegate<T> {
    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(RecyViewHolder ViewHolder, T t, int position);
}
