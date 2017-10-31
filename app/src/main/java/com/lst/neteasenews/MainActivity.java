package com.lst.neteasenews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.lst.neteasenews.news.fragment.EmptyFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mTitleList;
    private int[] mIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 数据源
        mTitleList = new ArrayList();
        mTitleList.add("新闻");
        mTitleList.add("阅读");
        mTitleList.add("视频");
        mTitleList.add("话题");
        mTitleList.add("我");

        mIcons = new int[]{R.drawable.news_selector,R.drawable.reading_selector,R.drawable.video_selector,R.drawable.topic_selector,R.drawable.mine_selector};


        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tab_Host);

        tabHost.setup(this, getSupportFragmentManager(), R.id.content);

        TabHost.TabSpec one = tabHost.newTabSpec("0");
        one.setIndicator(getViewWithIndex(this, 0));

        TabHost.TabSpec two = tabHost.newTabSpec("1");
        two.setIndicator(getViewWithIndex(this, 1));

        TabHost.TabSpec three = tabHost.newTabSpec("2");
        three.setIndicator(getViewWithIndex(this, 2));

        TabHost.TabSpec four = tabHost.newTabSpec("3");
        four.setIndicator(getViewWithIndex(this, 3));


        TabHost.TabSpec five = tabHost.newTabSpec("4");
        five.setIndicator(getViewWithIndex(this, 4));

        tabHost.addTab(one, EmptyFragment.class, null);
        tabHost.addTab(two, EmptyFragment.class, null);
        tabHost.addTab(three, EmptyFragment.class, null);
        tabHost.addTab(four, EmptyFragment.class, null);
        tabHost.addTab(five, EmptyFragment.class, null);


    }

    private View getViewWithIndex(Context context, int index) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View title_view = inflater.inflate(R.layout.item_title,null);
        TextView title = (TextView) title_view.findViewById(R.id.title);
        ImageView icon = (ImageView) title_view.findViewById(R.id.icon);
        // 设置标签的内容
        title.setText(mTitleList.get(index));
        icon.setImageResource(mIcons[index]);
        return title_view;
    }
}
