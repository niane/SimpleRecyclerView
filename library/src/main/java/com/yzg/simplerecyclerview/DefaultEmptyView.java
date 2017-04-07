package com.yzg.simplerecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by yzg on 2017/3/2.
 */

public class DefaultEmptyView extends FrameLayout implements SimpleRecyEmpty {
    private ImageView imgError, imgEmpty;
    private TextView txtLoading;
    private ProgressBar progressBar;

    public DefaultEmptyView(Context context) {
        this(context, null);
    }

    public DefaultEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.empty_page, this, true);

        imgError = (ImageView) view.findViewById(R.id.img_error);
        imgEmpty = (ImageView) view.findViewById(R.id.img_empty);
        txtLoading = (TextView) view.findViewById(R.id.txt_loading);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_proBar);
    }

    @Override
    public void onRefreshError() {
        txtLoading.setText("出错了");
        imgError.setVisibility(VISIBLE);
        imgEmpty.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onEmpty() {
        txtLoading.setText("暂无数据");
        imgEmpty.setVisibility(VISIBLE);
        imgError.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }

    @Override
    public void onLoading() {
        txtLoading.setText("正在加载");
        imgEmpty.setVisibility(GONE);
        imgError.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }
}
