package com.yzg.simplerecyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by yzg on 2017/3/31.
 */

public abstract class SimpleRecyAdapter<T> extends SimpleMultiRecyAdapter<T> {
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public SimpleRecyAdapter(final Context context, final int layoutId, List<T> datas)
    {
        super(context, datas);
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;

        addItemViewDelegate(new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(RecyViewHolder viewHolder, T t, int position)
            {
                SimpleRecyAdapter.this.convert(viewHolder, t, position);
            }
        });
    }

    protected abstract void convert(RecyViewHolder viewHolder, T t, int position);
}
