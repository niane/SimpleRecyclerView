package com.yzg.recydemo;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yzg.simplerecyclerview.SimpleRecyclerView;
import com.yzg.simplerecyclerview.adapter.RecyViewHolder;
import com.yzg.simplerecyclerview.adapter.SimpleRecyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SimpleRecyclerView simpleRecyclerView;
    private FloatingActionButton floatBtn;

    private List<String> mList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    private static final int OK = 0;
    private static final int ERROR = 1;
    private static final int EMPRTY = 2;
    private static final int OVER = 3;
    private static final int OK_1 = 4;
    private static final int OK_2 = 5;
    private static final int OVER_1 = 6;
    private static final int ERROR_1 = 7;

    private int pageNO = 0;
    private final int paseSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatBtn = (FloatingActionButton) findViewById(R.id.float_btn);
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simpleRecyclerView.getStatus() != SimpleRecyclerView.STATUS_REFRESHING){
                    mList.clear();
                    //须先清楚数据
                    simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_REFRESHING);
                    pageNO = 0;
                    requestDataList();
                }
            }
        });

        simpleRecyclerView = (SimpleRecyclerView) findViewById(R.id.simple_recycler_view);
        adapter = new SimpleRecyAdapter<String>(this, R.layout.listview_item, mList) {
            @Override
            protected void convert(RecyViewHolder holder, String string, int position) {
                holder.setText(R.id.text, string);
            }
        };

        simpleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        simpleRecyclerView.setAdapter(adapter);
        simpleRecyclerView.setOnLoadListener(new SimpleRecyclerView.OnLoadListener() {
            @Override
            public void onLoadMore() {
                simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_LOADING_MORE);
                requestDataList();
            }

            @Override
            public void onRefresh() {
                simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_REFRESHING);
                pageNO = 0;
                requestDataList();
            }
        });
        simpleRecyclerView.addHeaderView(R.layout.header_layout);
        simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_REFRESHING);
        requestDataList();

    }

    private void requestDataList() {
        simpleRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int state = new Random(System.currentTimeMillis()).nextInt(100)%8;
                int size = 0;
                switch (state){
                    case OK:
                    case OK_1:
                    case OK_2:
                        if(pageNO == 0){
                            mList.clear();
                        }

                        size = mList.size();
                        for (int i = size; i < size + paseSize; i++) {
                            mList.add("Item " + i);
                        }
                        simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_DEFAULT);
                        simpleRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "成功请求了" + paseSize + "条数据", Toast.LENGTH_SHORT).show();
                            }
                        });
                        pageNO++;
                        break;
                    case EMPRTY:
                        if(pageNO == 0){
                            mList.clear();
                            simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_REFRESH_EMPTY);
                        }else {
                            simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_LOAD_MORE_OVER);
                        }
                        break;
                    case ERROR:
                        if(pageNO == 0){
                            mList.clear();
                            simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_NETWORK_ERROR);
                        }else {
                            simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_LOAD_MORE_ERROR, "出错了，点击重试");
                        }
                        break;
                    case ERROR_1:
                        if(pageNO == 0){
                            mList.clear();
                            simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_REFRESH_ERROR, "出错了，点击重试");
                        }else {
                            simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_LOAD_MORE_ERROR, "出错了，点击重试");
                        }
                        break;
                    case OVER:
                    case OVER_1:
                        if(pageNO == 0){
                            mList.clear();
                        }
                        size = mList.size();
                        final int num = new Random().nextInt(paseSize - 5) + 3;
                        for (int i = size; i < size + num; i++) {
                            mList.add("Item " + i);
                        }
                        simpleRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "成功请求了" + num + "条数据", Toast.LENGTH_SHORT).show();
                            }
                        });

                        pageNO++;
                        simpleRecyclerView.setStatus(SimpleRecyclerView.STATUS_LOAD_MORE_OVER);
                        break;
                }

                adapter.notifyDataSetChanged();
            }
        }, 3000);
    }
}
