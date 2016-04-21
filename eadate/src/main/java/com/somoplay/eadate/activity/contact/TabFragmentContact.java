package com.somoplay.eadate.activity.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.somoplay.eadate.activity.MainActivity;
import com.somoplay.eadate.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * Created by yaolu on 15-09-14.
 */
public class TabFragmentContact extends Fragment implements View.OnClickListener {



//    private String mTitle = "Default";
//
    public static final String TITLE = "title";
    public static final String CONTEXT = "context";

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mBut2, mBut3, mBut4, mBut5, mBut6;
    private ImageButton mBackImg;
    private TextView mTitle;
    private View view=null;
    private Context context =null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_contact, null);
        initView();
        if (getArguments() != null)
        {
            mTitle.setText(getArguments().getString(TITLE));
        }

        return view;

    }

    private void initView(){
        mBut2 = (Button) view.findViewById(R.id.bt2);
        mBut3 = (Button) view.findViewById(R.id.bt3);
        mBut3.setVisibility(View.GONE);
        mBut4 = (Button) view.findViewById(R.id.bt4);
        mBut5 = (Button) view.findViewById(R.id.bt5);
        mBut6 = (Button) view.findViewById(R.id.bt6);
        mBackImg = (ImageButton)view.findViewById(R.id.back);
        mTitle = (TextView) view.findViewById(R.id.txt1);
        context=getActivity();

       mBackImg.setVisibility(View.GONE);
        mTitle.setText("Contact");
        mBut6.setVisibility(View.GONE);

        mBut2.setOnClickListener(this);
        mBut3.setOnClickListener(this);
        mBut4.setOnClickListener(this);
        mBut5.setOnClickListener(this);
        mBut6.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt2:
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(context, "2", "Test");
                break;
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
}
