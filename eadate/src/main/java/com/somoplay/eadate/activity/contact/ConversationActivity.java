package com.somoplay.eadate.activity.contact;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.somoplay.eadate.R;

/**
 * Created by Frank on 15/12/6.
 */
public class ConversationActivity extends FragmentActivity {
    private static final String TAG = ConversationActivity.class.getSimpleName();

    private TextView mTitle;
    private ImageButton mBack;
    private ImageButton mMore;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //唯一有用的代码，加载一个 layout
        setContentView(R.layout.fragment_conversation);
        //继承的是ActionBarActivity，直接调用 自带的 Actionbar，下面是Actionbar 的配置，如果不用可忽略…
//        getSupportActionBar().setTitle("聊天");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.rc_bar_back);

        setActionBar();
    }


    /**
     * 设置 actionbar 事件
     */
    private void setActionBar() {

        mTitle = (TextView) findViewById(R.id.txt1);
        mTitle.setText("聊天界面");
        mBack = (ImageButton) findViewById(R.id.back);
        mMore =(ImageButton) findViewById(R.id.more);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
