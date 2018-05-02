package com.comtop.eimnote.widget.tag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comtop.eimnote.R;

/**
 * Created by chenxiaojian on 16/3/29.
 */
public class SearchItemView extends LinearLayout {
    private TextView tvName;
    private ImageView ivCancel;
    public SearchItemView(Context context) {
        super(context);
        initView();
    }

    public SearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        View.inflate(getContext(), R.layout.search_item_view,this);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivCancel = (ImageView) findViewById(R.id.iv_cancel);
    }

    public void setName(String name){
        tvName.setText(name);
    }

    public View getCancelView(){
        return ivCancel;
    }

}
