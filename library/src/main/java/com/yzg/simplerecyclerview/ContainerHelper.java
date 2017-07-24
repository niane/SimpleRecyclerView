package com.yzg.simplerecyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by yzg on 2017/3/30.
 */

class ContainerHelper implements IContainer{

    private LinearLayout mHeaderContainer;

    private LinearLayout mFooterContainer;

    private FrameLayout mLoadMoreFooterContainer;

    private FrameLayout mEmptyContainer;

    private FrameLayout tmpLoadMoreFooterContainer;

    private FrameLayout tmpEmptyContainer;

    private Context mContext;
    private LayoutInflater inflater;
    private SimpleRecyclerView.OnLoadListener onLoadListener;
    private @SimpleRecyclerView.Status int mStatus = SimpleRecyclerView.STATUS_DEFAULT;

    public ContainerHelper(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void addHeaderView(View view) {
        if(view == null) return;
        ensureHeaderContainer();
        mHeaderContainer.addView(view);
    }

    @Override
    public void addHeaderView(@LayoutRes int resLayout) {
        if(resLayout < 0) return;
        addHeaderView(inflater.inflate(resLayout, mHeaderContainer, false));
    }

    @Override
    public void removeHeaderView(View view) {
        if(mHeaderContainer != null && view.getParent() == mHeaderContainer){
            mHeaderContainer.removeView(view);
        }
    }

    @Override
    public void addFooterView(View view) {
        if(view == null) return;
        ensureFooterContainer();
        mFooterContainer.addView(view);
    }

    @Override
    public void addFooterView(@LayoutRes int resLayout) {
        if(resLayout < 0) return;
        addFooterView(inflater.inflate(resLayout, mFooterContainer, false));
    }

    @Override
    public void removeFooterView(View view) {
        if(mFooterContainer != null && view.getParent() == mFooterContainer){
            mFooterContainer.removeView(view);
        }
    }

    @Override
    public void setLoadMoreView(View view) {
        if(view == null) return;

        if(!(view instanceof SimpleRecyLoadMore)){
            throw new IllegalArgumentException("Load more View must implement SimpleRecyLoadMore");
        }

        ensureLoadMoreFooterContainer();
        tmpLoadMoreFooterContainer.removeAllViews();
        tmpLoadMoreFooterContainer.addView(view);
    }

    @Override
    public void setLoadMoreView(@LayoutRes int resLayout) {
        if(resLayout < 0) return;

        setLoadMoreView(inflater.inflate(resLayout, mLoadMoreFooterContainer, false));
    }

    @Override
    public void setEmptyView(View view) {
        if(view == null) return;

        if(!(view instanceof SimpleRecyEmpty)){
            throw new IllegalArgumentException("Empty View must implement SimpleRecyEmpty");
        }

        ensureEmptyContainer();
        tmpEmptyContainer.removeAllViews();
        tmpEmptyContainer.addView(view);
    }

    @Override
    public void setEmptyView(@LayoutRes int resLayout) {
        if(resLayout < 0) return;

        setEmptyView(inflater.inflate(resLayout, mEmptyContainer, false));
    }

    @Override
    public boolean hasHeader() {
        return mHeaderContainer != null && mHeaderContainer.getChildCount() > 0;
    }

    @Override
    public boolean hasFooter() {
        return mFooterContainer != null && mFooterContainer.getChildCount() > 0;
    }

    @Override
    public boolean hasLoadMore() {
        return mLoadMoreFooterContainer != null && mLoadMoreFooterContainer.getChildCount() > 0;
    }

    @Override
    public boolean hasEmpty() {
        return mEmptyContainer != null && mEmptyContainer.getChildCount() > 0;
    }

    @Override
    public void setStatus(@SimpleRecyclerView.Status int status) {
        setStatus(status, "");
    }

    @Override
    public void setStatus(@SimpleRecyclerView.Status int status, String msg) {
        mStatus = status;
        switch (status){
            case SimpleRecyclerView.STATUS_DEFAULT:
                mEmptyContainer = null;
                mLoadMoreFooterContainer = null;
                break;
            case SimpleRecyclerView.STATUS_REFRESHING:
                mLoadMoreFooterContainer = null;
                mEmptyContainer = tmpEmptyContainer;
                if(hasEmpty()){
                    SimpleRecyEmpty simpleRecyEmpty = (SimpleRecyEmpty) mEmptyContainer.getChildAt(0);
                    simpleRecyEmpty.onLoading(msg);
                    mEmptyContainer.setOnClickListener(null);
                }
                break;
            case SimpleRecyclerView.STATUS_REFRESH_ERROR:
                mLoadMoreFooterContainer = null;
                mEmptyContainer = tmpEmptyContainer;
                if(hasEmpty()){
                    final SimpleRecyEmpty simpleRecyEmpty = (SimpleRecyEmpty) mEmptyContainer.getChildAt(0);
                    simpleRecyEmpty.onRefreshError(msg);
                    mEmptyContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onLoadListener != null){
//                                simpleRecyEmpty.onLoading();
                                onLoadListener.onRefresh();
                            }
                        }
                    });
                }
                break;
            case SimpleRecyclerView.STATUS_REFRESH_EMPTY:
                mLoadMoreFooterContainer = null;
                mEmptyContainer = tmpEmptyContainer;
                if(hasEmpty()){
                    final SimpleRecyEmpty simpleRecyEmpty = (SimpleRecyEmpty) mEmptyContainer.getChildAt(0);
                    simpleRecyEmpty.onEmpty(msg);
                    mEmptyContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onLoadListener != null){
//                                simpleRecyEmpty.onLoading();
                                onLoadListener.onRefresh();
                            }
                        }
                    });
                }
                break;
            case SimpleRecyclerView.STATUS_LOADING_MORE:
                mEmptyContainer = null;
                mLoadMoreFooterContainer = tmpLoadMoreFooterContainer;
                if(hasLoadMore()){
                    SimpleRecyLoadMore simpleRecyLoadMore = (SimpleRecyLoadMore) mLoadMoreFooterContainer.getChildAt(0);
                    simpleRecyLoadMore.onLoading(msg);
                    mLoadMoreFooterContainer.setOnClickListener(null);
                }
                break;
            case SimpleRecyclerView.STATUS_LOAD_MORE_OVER:
                mEmptyContainer = null;
                mLoadMoreFooterContainer = tmpLoadMoreFooterContainer;
                if(hasLoadMore()){
                    SimpleRecyLoadMore simpleRecyLoadMore = (SimpleRecyLoadMore) mLoadMoreFooterContainer.getChildAt(0);
                    simpleRecyLoadMore.onLoadOver(msg);
                    mLoadMoreFooterContainer.setOnClickListener(null);
                }
                break;
            case SimpleRecyclerView.STATUS_LOAD_MORE_ERROR:
                mEmptyContainer = null;
                mLoadMoreFooterContainer = tmpLoadMoreFooterContainer;
                if(hasLoadMore()){
                    SimpleRecyLoadMore simpleRecyLoadMore = (SimpleRecyLoadMore) mLoadMoreFooterContainer.getChildAt(0);
                    simpleRecyLoadMore.onLoadError(msg);
                    mLoadMoreFooterContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onLoadListener != null){
//                                setStatus(SimpleRecyclerView.STATUS_LOADING_MORE);
                                onLoadListener.onLoadMore();
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public void setOnLoadListener(SimpleRecyclerView.OnLoadListener onLoadMoreListener) {
        this.onLoadListener = onLoadMoreListener;
    }

    private void ensureHeaderContainer() {
        if (mHeaderContainer == null) {
            mHeaderContainer = new LinearLayout(mContext);
            mHeaderContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private void ensureFooterContainer() {
        if (mFooterContainer == null) {
            mFooterContainer = new LinearLayout(mContext);
            mFooterContainer.setOrientation(LinearLayout.VERTICAL);
            mFooterContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private void ensureLoadMoreFooterContainer() {
        if (tmpLoadMoreFooterContainer == null) {
            tmpLoadMoreFooterContainer = new FrameLayout(mContext);
            tmpLoadMoreFooterContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    private void ensureEmptyContainer() {
        if (tmpEmptyContainer == null) {
            tmpEmptyContainer = new FrameLayout(mContext);
            tmpEmptyContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    public LinearLayout getHeaderContainer() {
        return mHeaderContainer;
    }

    public LinearLayout getFooterContainer() {
        return mFooterContainer;
    }

    public FrameLayout getLoadMoreFooterContainer() {
        return mLoadMoreFooterContainer;
    }

    public FrameLayout getEmptyContainer() {
        return mEmptyContainer;
    }
}
