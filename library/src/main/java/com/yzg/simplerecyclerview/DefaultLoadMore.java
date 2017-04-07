package com.yzg.simplerecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by yzg on 2017/3/6.
 */

public class DefaultLoadMore extends FrameLayout implements SimpleRecyLoadMore {
    private ProgressBar progressBar;
    private TextView textView;

    public DefaultLoadMore(Context context) {
        this(context, null);
    }

    public DefaultLoadMore(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultLoadMore(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.load_more_layout, this, true);

        progressBar = (ProgressBar) view.findViewById(R.id.load_more_proBar);
        textView = (TextView) view.findViewById(R.id.load_more_txt);
    }

    @Override
    public void onLoading() {
        textView.setText("加载中...");
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadOver() {
        textView.setText("到底了");
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onLoadError() {
        textView.setText("出错了");
        progressBar.setVisibility(GONE);
    }
}
