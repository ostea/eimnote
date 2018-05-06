package com.comtop.eimnote.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comtop.eimnote.NoteApp;
import com.comtop.eimnote.R;
import com.comtop.eimnote.base.BaseActivity;
import com.comtop.eimnote.model.LabellModel;
import com.comtop.eimnote.util.CLog;
import com.comtop.eimnote.util.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LabelSelectorActiviy extends BaseActivity implements AdapterView.OnItemClickListener {


    private ListView mListView;
    private LabelAdapter mAdapter;

    private RelativeLayout rl_label;
    private HorizontalScrollView hsv;
    private LinearLayout ll_content;
    private Button btn_clear;

    private List<LabellModel> labelList = new ArrayList<>();
    private HashMap<Integer, View> mSelectMap = new HashMap<Integer, View>();
    private ArrayList<LabellModel> mSelectedLabel = new ArrayList<>();

    private int mScreenWidth;
    private LayoutInflater mInflater;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: 2018/5/5  标签CRUD后刷新

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_selector);
        initView();
        IntentFilter filter = new IntentFilter();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    private void initIntentData() {
        mSelectedLabel = (ArrayList<LabellModel>) getIntent().getSerializableExtra("selectedLabel");
        if (mSelectedLabel != null && mSelectMap.size() > 0) {
            for (LabellModel model : mSelectedLabel) {
                addSelectedView(model);
            }
        }
    }

    private void initView() {
        showProgress();
        mInflater = LayoutInflater.from(this);
        rl_label = findViewById(R.id.rl_label);
        hsv = findViewById(R.id.hsv);
        ll_content = findViewById(R.id.ll_content);
        btn_clear = findViewById(R.id.btn_clear_label);

        mListView = findViewById(R.id.list);
        generaDummyData();
        mAdapter = new LabelAdapter(labelList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mScreenWidth = ScreenUtils.getScreenWidth(NoteApp.getContext());
        findViewById(R.id.btn_clear_label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clearAllState();
                // test activity result
                Intent data = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable("selectedLabel", mSelectedLabel);
                data.putExtras(bundle);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        updateUI();
    }

    private void updateUI() {
        mAdapter.notifyDataSetChanged();
        if (mSelectMap.isEmpty()) {
            rl_label.setVisibility(View.GONE);
        } else {
            rl_label.setVisibility(View.VISIBLE);
        }
        dismissPd();
    }

    private void clearAllState() {
        mSelectMap.clear();
        mSelectedLabel.clear();
        showProgress();
        ll_content.removeAllViews();
        mAdapter.notifyDataSetChanged();
        dismissPd();
        rl_label.setVisibility(View.GONE);
    }

    private void generaDummyData() {
        for (int i = 0; i < 100; i++) {
            LabellModel model = new LabellModel(i, "标签 >  " + i);
            labelList.add(model);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getContext() == null) {
            return;
        }
        if (mAdapter == null) {
            return;
        }
        final LabellModel labellModel = ((LabellModel) parent.getAdapter().getItem(position));
        LabelViewHolder holder = (LabelViewHolder) view.getTag();
        if (labellModel.isSelector()) {
            labellModel.setSelector(false);
            holder.iv_selected.setImageResource(R.drawable.cellnot_selected);
        } else {
            labellModel.setSelector(true);
            holder.iv_selected.setImageResource(R.drawable.cellblue_selected);
        }
        addSelectedView(labellModel);
    }

    private void addSelectedView(LabellModel data) {
        Integer tagId = data.getTagId();
        if (mSelectMap.containsKey(tagId)) {
            View view = mSelectMap.get(tagId);
            ll_content.removeView(view);
            mSelectMap.remove(tagId);
            mSelectedLabel.remove(data);
            measureHsvWidth();
            return;
        }
        TextView textView = (TextView) mInflater.inflate(R.layout.view_label_item, ll_content, false);
        textView.setTag(R.id.ll_content, data.getTagId());
        textView.setText(data.getName());
        ll_content.addView(textView);
        mSelectMap.put(tagId, textView);
        mSelectedLabel.add(data);
        measureHsvWidth();
    }

    private void measureHsvWidth() {
        mAdapter.notifyDataSetChanged();
        if (mSelectMap.isEmpty()) {
            rl_label.setVisibility(View.GONE);
            return;
        }
        rl_label.setVisibility(View.VISIBLE);
        ll_content.post(new Runnable() {
            @Override
            public void run() {
                CLog.e("tag", "hsv.getMeasuredWidth() " + hsv.getMeasuredWidth()
                        + " mScreenWidth * 0.75 " + mScreenWidth * 0.9
                        + "  ll_content.getMeasuredWidth() "
                        + ll_content.getMeasuredWidth());
                if (hsv.getMeasuredWidth() >= mScreenWidth * 0.9) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            (int) (mScreenWidth * 0.9),
                            RelativeLayout.LayoutParams.MATCH_PARENT);
                    hsv.setLayoutParams(layoutParams);
                    hsv.fullScroll(View.FOCUS_RIGHT);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
                    hsv.setLayoutParams(layoutParams);
                }
            }
        });
    }

    private class LabelAdapter extends BaseAdapter {
        private List<LabellModel> modelList;

        public LabelAdapter(List<LabellModel> modelList) {
            this.modelList = modelList;
        }

        public void updateListview(List<LabellModel> modelList) {
            this.modelList = modelList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return modelList != null ? modelList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return modelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LabelViewHolder holder;
            final LabellModel model = (LabellModel) getItem(position);
            if (convertView == null) {
                holder = new LabelViewHolder();
                convertView = View.inflate(LabelSelectorActiviy.this, R.layout.item_label_selector, null);
                holder.tv_label = convertView.findViewById(R.id.tv_label);
                holder.iv_selected = convertView.findViewById(R.id.iv_selector);
                convertView.setTag(holder);
            } else {
                holder = (LabelViewHolder) convertView.getTag();
            }
            if (!TextUtils.isEmpty(model.getName())) {
                holder.tv_label.setText(model.getName());
            }
            if (model.isSelector()) {
                holder.iv_selected.setImageResource(R.drawable.cellblue_selected);
            } else {
                holder.iv_selected.setImageResource(R.drawable.cellnot_selected);
            }
            if (mSelectMap.containsKey(model.getTagId())) {
                holder.iv_selected.setImageResource(R.drawable.cellblue_selected);
            } else {
                holder.iv_selected.setImageResource(R.drawable.cellnot_selected);
            }
            return convertView;
        }
    }

    private static class LabelViewHolder {
        TextView tv_label;
        ImageView iv_selected;
    }

}
