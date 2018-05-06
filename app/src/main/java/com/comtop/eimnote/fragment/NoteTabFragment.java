package com.comtop.eimnote.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.comtop.eimnote.MainActivity;
import com.comtop.eimnote.R;
import com.comtop.eimnote.base.BaseFragment;
import com.comtop.eimnote.util.CLog;


public class NoteTabFragment extends BaseFragment implements MainActivity.SearchAction {

    private static final String TAG = NoteTabFragment.class.getSimpleName();
    public static final int CATEGORY_ALL = 1 << 1;
    public static final int CATEGORY_MINE = 1 << 2;
    public static final int CATEGORY_SHARE = 1 << 3;

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

    private int mCategory = CATEGORY_ALL;
    private View mView;
    private TextView tv_desc;

    public static Fragment newInstance(int category) {
        NoteTabFragment fragment = new NoteTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_CATALOG, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment instantiate(Context context, int catalog) {
        Fragment fragment = new NoteTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_CATALOG, catalog);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
            mCategory = data.getInt(BUNDLE_KEY_CATALOG, CATEGORY_MINE);
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
        tv_desc.setText(mCategory + ">>>>");
    }

    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        CLog.i(TAG, "lazyload");
    }

    @Override
    protected void loadWhenInvisible() {
        super.loadWhenInvisible();
        CLog.i(TAG, "c");

    }

    @Override
    protected void loadWhenVisible() {
        super.loadWhenVisible();
        CLog.i(TAG, "loadWhenVisible");
        showProgress();
    }

    @Override
    public void search(String content) {
        CLog.i(TAG, mCategory+"---->" + content);
    }

}
