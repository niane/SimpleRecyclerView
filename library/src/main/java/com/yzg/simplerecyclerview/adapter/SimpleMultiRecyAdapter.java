package com.yzg.simplerecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yzg on 2017/3/20.
 */

public class SimpleMultiRecyAdapter<T> extends RecyclerView.Adapter<RecyViewHolder> {
    private Context mContext;
    private List<T> dataList;

    protected ItemViewDelegateManager mItemViewDelegateManager;

    public SimpleMultiRecyAdapter(Context mContext, List<T> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;

        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(dataList.get(position), position);
    }

    @Override
    public RecyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        RecyViewHolder holder = RecyViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyViewHolder holder, int position) {
        convert(holder, dataList.get(position));
    }

    public void onViewHolderCreated(RecyViewHolder holder, View itemView){

    }

    public void convert(RecyViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }


    public SimpleMultiRecyAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public SimpleMultiRecyAdapter addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }
}
