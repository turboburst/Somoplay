package com.somoplay.eadate.activity.message;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.somoplay.eadate.activity.MainActivity;
import com.somoplay.eadate.R;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;


/**
 * Created by yaolu on 15-09-14.
 */
public class TabFragmentMessage extends Fragment implements View.OnClickListener {


    //    private String mTitle = "Default";
//
    public static final String TITLE = "title";
    public static final String CONTEXT = "context";

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mBut2, mBut3, mBut4, mBut5, mBut6;
    private ImageButton mBackImg;
    private TextView mTitle;
    private View view = null;
    private Context context = null;
    private ConversationListFragment fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conversationlist, container,false);
        }
        initView();

        enterFragment();
        if (getArguments() != null) {
            mTitle.setText(getArguments().getString(TITLE));
        }


        return view;

    }

    private void initView() {
        mBackImg = (ImageButton) view.findViewById(R.id.back);
        mTitle = (TextView) view.findViewById(R.id.txt1);
        context = getActivity();

        mBackImg.setVisibility(View.GONE);
        mTitle.setText("Message");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt3:
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startConversationList(context);
                break;
            case R.id.bt4:
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startSubConversationList(context, Conversation.ConversationType.GROUP);
                break;
            case R.id.bt5:
//                startActivity(new Intent(context, ContactsActivity.class));
                break;
        }
    }

    /**
     * 加载 会话列表 ConversationListFragment
     * 静态集成
     */
    private void enterFragment() {

        fragment = (ConversationListFragment) getChildFragmentManager().findFragmentById(R.id.conversationlist);
        Log.d(TAG, "rong://" + context.getApplicationInfo().packageName);
        Uri uri = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }



}
