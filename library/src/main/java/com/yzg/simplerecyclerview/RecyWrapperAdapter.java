package com.yzg.simplerecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yzg on 2017/3/3.
 */

class RecyWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected static final int HEADER = Integer.MIN_VALUE;
    protected static final int EMPTY = Integer.MIN_VALUE + 1;
    protected static final int FOOTER = Integer.MAX_VALUE - 1;
    protected static final int LOAD_MORE_FOOTER = Integer.MAX_VALUE;

    private final RecyclerView.Adapter mAdapter;
    private ContainerHelper mContainerHelper;

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            int start = mContainerHelper.hasHeader() ? 1 : 0;
            notifyItemRangeChanged(positionStart + start, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            int start = mContainerHelper.hasHeader() ? 1 : 0;
            notifyItemRangeChanged(positionStart + start, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            int start = mContainerHelper.hasHeader() ? 1 : 0;
            notifyItemRangeInserted(positionStart + start, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            int start = mContainerHelper.hasHeader() ? 1 : 0;
            notifyItemRangeRemoved(positionStart + start, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int start = mContainerHelper.hasHeader() ? 1 : 0;
            notifyItemMoved(fromPosition + start, toPosition + start);
        }
    };

    public RecyWrapperAdapter(RecyclerView.Adapter adapter, ContainerHelper containerHelper) {
        this.mAdapter = adapter;
        this.mContainerHelper = containerHelper;

        mAdapter.registerAdapterDataObserver(observer);
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    RecyWrapperAdapter wrapperAdapter = (RecyWrapperAdapter) recyclerView.getAdapter();
                    if (isFullSpanType(wrapperAdapter.getItemViewType(position))) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        int type = getItemViewType(position);
        if (isFullSpanType(type)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                lp.setFullSpan(true);
            }
        }
    }

    private boolean isFullSpanType(int type) {
        return type == HEADER || type == EMPTY || type == FOOTER || type == LOAD_MORE_FOOTER;
    }

    @Override
    public int getItemCount() {
        int count = mAdapter.getItemCount();

        if(mContainerHelper.hasEmpty()) count++;
        if(mContainerHelper.hasLoadMore()) count++;
        if(mContainerHelper.hasFooter()) count++;
        if(mContainerHelper.hasHeader()) count++;

        return count;
    }

    @Override
    public int getItemViewType(int position) {

        if (mContainerHelper.hasEmpty()) {
            switch (position) {
                case 0:
                    if (mContainerHelper.hasHeader()) {
                        return HEADER;
                    } else {
                        return EMPTY;
                    }
                case 1:
                    if (mContainerHelper.hasHeader()) {
                        return EMPTY;
                    } else {
                        return FOOTER;
                    }
                case 2:
                    return FOOTER;
                default:
                    return EMPTY;
            }
        }
        int numHeaders = mContainerHelper.hasHeader() ? 1 : 0;
        if (position < numHeaders) {
            return HEADER;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            } else {
                adjPosition = adjPosition - adapterCount;
                int numFooters = mContainerHelper.hasFooter() ? 1 : 0;
                if (adjPosition < numFooters) {
                    return FOOTER;
                } else {
                    return LOAD_MORE_FOOTER;
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new HeaderContainerViewHolder(mContainerHelper.getHeaderContainer());
        } else if(viewType == EMPTY){
            return new EmptyPageContainerViewHolder(mContainerHelper.getEmptyContainer());
        } else if (viewType == FOOTER) {
            return new FooterContainerViewHolder(mContainerHelper.getFooterContainer());
        } else if (viewType == LOAD_MORE_FOOTER) {
            return new LoadMoreFooterContainerViewHolder(mContainerHelper.getLoadMoreFooterContainer());
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        int numHeaders = mContainerHelper.hasHeader() ? 1 : 0;

        switch (viewType) {
            case LOAD_MORE_FOOTER:
                break;
            case HEADER:
                break;
            case EMPTY:
                break;
            case FOOTER:
                break;
            default:
                mAdapter.onBindViewHolder(holder, position - numHeaders);
                break;
        }
    }

    static class HeaderContainerViewHolder extends RecyclerView.ViewHolder {

        public HeaderContainerViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class EmptyPageContainerViewHolder extends RecyclerView.ViewHolder{
        public EmptyPageContainerViewHolder(View itemView){ super(itemView);}
    }

    static class FooterContainerViewHolder extends RecyclerView.ViewHolder {

        public FooterContainerViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class LoadMoreFooterContainerViewHolder extends RecyclerView.ViewHolder {

        public LoadMoreFooterContainerViewHolder(View itemView) {
            super(itemView);
        }
    }


}
