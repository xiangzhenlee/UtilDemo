package com.yushan.utildemo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private QuickIndexBar quickIndexBar;
    private ListView listview;
    private TextView currentWord;
    private ArrayList<String> friends = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        // 对数据进行排序
        Collections.sort(friends);

        listview.setAdapter(new FriendAdapter(this, friends));
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        currentWord = (TextView) findViewById(R.id.currentWord);

        quickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
        quickIndexBar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //根据触摸的字母去集合中找首字母和letter相同的条目，然后将条目置顶
                for (int i = 0; i < friends.size(); i++) {
                    String word = PinYinUtil.getPinYin(friends.get(i)).charAt(0) + "";
                    if (word.equals(letter)) {
                        //说明当前的i就是需要的，则直接置顶
                        listview.setSelection(i);
                        //由于只需要找到第一个就行了，所以要中断
                        quickIndexBar.showCurrentWord(currentWord, letter);
                        break;
                    }
                }
            }
        });
    }

    private void initData() {

        friends.add("33浪花");
        friends.add("李伟");
        friends.add("张三");
        friends.add("阿三");
        friends.add("阿四");
        friends.add("段誉");
        friends.add("段正淳");
        friends.add("张三丰");
        friends.add("陈坤");
        friends.add("林俊杰1");
        friends.add("陈坤2");
        friends.add("王二a");
        friends.add("林俊杰a");
        friends.add("张四");
        friends.add("林俊杰");
        friends.add("王二");
        friends.add("王二b");
        friends.add("赵四");
        friends.add("杨坤");
        friends.add("赵子龙");
        friends.add("杨坤1");
        friends.add("李伟1");
        friends.add("宋江");
        friends.add("宋江1");
        friends.add("李伟3");
    }
}
