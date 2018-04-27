package com.comtop.eimnote.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comtop.eimnote.R;
import com.comtop.eimnote.fragment.base.BaseFragment;
import com.comtop.eimnote.util.CLog;


public class NoteTabFragment extends BaseFragment {

    private static final String TAG = NoteTabFragment.class.getSimpleName();
    public static final int CATEGORY_ALL = 1 << 1;
    public static final int CATEGORY_MINE = 1 << 2;
    public static final int CATEGORY_SHARE = 1 << 3;

    private int mCategory = CATEGORY_ALL;
    private View mView;
    private TextView tv_desc;

    public static Fragment newInstance(int category) {
        NoteTabFragment fragment = new NoteTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mCategory = data.getInt("category");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_all_note, container, false);
        initView();
        CLog.i(TAG, TAG);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mCategory == CATEGORY_ALL) {
            setUserVisibleHint(true);
        }
    }

    private void initView() {
        tv_desc = mView.findViewById(R.id.tv_desc);
        tv_desc.setText(mCategory+">>>>");
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        CLog.i(TAG, "lazyload");
    }

    @Override
    protected void loadWhenInvisible() {
        super.loadWhenInvisible();
        CLog.i(TAG, "loadWhenInvisible");

    }

    @Override
    protected void loadWhenVisible() {
        super.loadWhenVisible();
        CLog.i(TAG, "loadWhenVisible");

    }

}
