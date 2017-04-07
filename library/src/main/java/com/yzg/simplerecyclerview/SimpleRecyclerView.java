package com.yzg.simplerecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yzg on 2017/3/3.
 */

public class SimpleRecyclerView extends RecyclerView {
    private static final String TAG = SimpleRecyclerView.class.getSimpleName();

    /**
     * 默认初始状态
     **/
    public static final int STATUS_DEFAULT = 0;
    /**
     * 正在刷新数据
     **/
    public static final int STATUS_REFRESHING = 1;
    /**
     * 刷新数据时出错
     **/
    public static final int STATUS_REFRESH_ERROR = 2;
    /**
     * 刷新数据时数据为空
     **/
    public static final int STATUS_REFRESH_EMPTY = 3;
    /**
     * 正在加载更多
     **/
    public static final int STATUS_LOADING_MORE = 4;
    /**
     * 所有数据都已加载
     **/
    public static final int STATUS_LOAD_MORE_OVER = 5;
    /**
     * 加载更多数据时出错
     **/
    public static final int STATUS_LOAD_MORE_ERROR = 6;

    @IntDef({STATUS_DEFAULT, STATUS_REFRESHING, STATUS_REFRESH_ERROR, STATUS_REFRESH_EMPTY, STATUS_LOADING_MORE, STATUS_LOAD_MORE_OVER, STATUS_LOAD_MORE_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status {
    }

    private ContainerHelper mContainerHelper;

    private boolean mLoadMoreEnbled;

    private OnLoadListener mOnLoadMoreListener;

    public SimpleRecyclerView(Context context) {
        this(context, null);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContainerHelper = new ContainerHelper(context);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleRecyclerView, defStyle, 0);

        mLoadMoreEnbled = a.getBoolean(R.styleable.SimpleRecyclerView_loadmore_enabled, true);

        int loadMoreFooterLayoutRes = a.getResourceId(R.styleable.SimpleRecyclerView_loadmore_layout, -1);
        int emptyPageLayoutRes = a.getResourceId(R.styleable.SimpleRecyclerView_empty_page_layout, -1);

        a.recycle();

        if (loadMoreFooterLayoutRes == -1) {
            mContainerHelper.setLoadMoreView(new DefaultLoadMore(context));
        } else {
            mContainerHelper.setLoadMoreView(loadMoreFooterLayoutRes);
        }

        if (emptyPageLayoutRes == -1) {
            mContainerHelper.setEmptyView(new DefaultEmptyView(context));
        } else {
            mContainerHelper.setEmptyView(emptyPageLayoutRes);
        }

        setStatus(STATUS_DEFAULT);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(getUserAdapter() == null) return;

                if (newState == RecyclerView.SCROLL_STATE_IDLE && getUserAdapter().getItemCount() > 0
                        && canTriggerLoadMore(recyclerView)) {

                    if (mLoadMoreEnbled && mOnLoadMoreListener != null && !mContainerHelper.hasLoadMore()) {
                        setStatus(STATUS_LOADING_MORE);
                        mOnLoadMoreListener.onLoadMore();
                        recyclerView.smoothScrollToPosition(getAdapter().getItemCount() - 1);
                    }
                }
            }
        });
    }

    public boolean canTriggerLoadMore(RecyclerView recyclerView) {
        View lastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        int position = recyclerView.getChildLayoutPosition(lastChild);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        return totalItemCount - 1 == position;
    }

    public void addHeaderView(View headerView) {
        if(headerView == null) return;
        mContainerHelper.addHeaderView(headerView);
        Adapter adapter = getAdapter();
        if (adapter != null) {
            adapter.notifyItemChanged(adapter.getItemCount() - 1);
        }
    }

    public void addHeaderView(@LayoutRes int resLayout) {
        mContainerHelper.addHeaderView(resLayout);
        Adapter adapter = getAdapter();
        if (adapter != null) {
            adapter.notifyItemChanged(adapter.getItemCount() - 1);
        }
    }

    public void addFooterView(View footerView) {
        if(footerView == null) return;
        mContainerHelper.addFooterView(footerView);
        Adapter adapter = getAdapter();
        if (adapter != null) {
            adapter.notifyItemChanged(adapter.getItemCount() - 1);
        }
    }

    public void addFooterView(@LayoutRes int resLayout) {
        mContainerHelper.addFooterView(resLayout);
        Adapter adapter = getAdapter();
        if (adapter != null) {
            adapter.notifyItemChanged(adapter.getItemCount() - 1);
        }
    }

    public void setLoadMoreView(View view){
        mContainerHelper.setLoadMoreView(view);
    }

    public void setLoadMoreView(@LayoutRes int resLayout){
        mContainerHelper.setLoadMoreView(resLayout);
    }

    public void setEmptyView(View view){
        mContainerHelper.setEmptyView(view);
    }

    public void setEmptyView(@LayoutRes int resLayout){
        mContainerHelper.setEmptyView(resLayout);
    }

    public Adapter getUserAdapter() {
        if(getAdapter() != null){
            return ((RecyWrapperAdapter) super.getAdapter()).getAdapter();
        }

        return null;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(new RecyWrapperAdapter(adapter, mContainerHelper));
    }

    public void enableLoadMore(boolean enabledLoadMore){
        mLoadMoreEnbled = enabledLoadMore;
    }

    public int getStatus() {
        return mContainerHelper.getStatus();
    }

    public void setStatus(@Status int status) {
        /**
         * 当status为 STATUS_REFRESHING || STATUS_REFRESH_EMPTY || STATUS_REFRESH_ERROR时，必须保证数据为空
         */
        if(status == STATUS_REFRESHING || status == STATUS_REFRESH_EMPTY || status == STATUS_REFRESH_ERROR){
            if(getUserAdapter() != null && getUserAdapter().getItemCount() != 0){
                mContainerHelper.setStatus(STATUS_DEFAULT);
            }else {
                mContainerHelper.setStatus(status);
            }
        }else {
            mContainerHelper.setStatus(status);
        }

        if (getAdapter() != null)
            getAdapter().notifyDataSetChanged();
    }

    public OnLoadListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    public void setOnLoadListener(OnLoadListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        mContainerHelper.setOnLoadListener(onLoadMoreListener);
    }

    public interface OnLoadListener {
        void onLoadMore();

        void onRefresh();
    }

}
