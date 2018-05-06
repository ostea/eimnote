package com.comtop.eimnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.comtop.eimnote.adapter.BaseRecyclerAdapter;
import com.comtop.eimnote.adapter.SearchHistoryAdapter;
import com.comtop.eimnote.cache.CacheManagerV2;
import com.comtop.eimnote.fragment.NoteTabFragment;
import com.comtop.eimnote.http.HttpService;
import com.comtop.eimnote.http.RetrofitManager;
import com.comtop.eimnote.model.LabellModel;
import com.comtop.eimnote.util.CLog;
import com.comtop.eimnote.widget.NotePagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_PAGE = "page";
    public static final String EXTRA_SOURCE = "source";
    public static final int TAB_ALL = 0;
    public static final int TAB_MINE = 1;
    public static final int TAB_SHARED = 2;

    private NotePagerSlidingTabStrip mTabs;
    private ViewPager viewPager;

    private List<Pair<String, Fragment>> mPagerItems;
    private Fragment allNoteFragment;
    private Fragment myNoteFragment;
    private Fragment shareNoteFragment;
    private NoteTabAdapter mTabAdapter;

    private int mCurrentFragmentIndex = 0;

    private final String[] tabs = new String[]{"全部笔记", "我的笔记", "分享笔记"};

    private ArrayList<LabellModel> mSelectedLabel = new ArrayList<>();

    private String mSearchText;
    private Runnable mSearchRunnable = new Runnable() {
        @Override
        public void run() {
            if (TextUtils.isEmpty(mSearchText)) {
                return;
            }
            SearchAction f = (SearchAction) mPagerItems.get(viewPager.getCurrentItem()).second;
            f.search(mSearchText);

            SearchHistoryAdapter.SearchItem item = new SearchHistoryAdapter.SearchItem(mSearchText);
            if (mAdapter.getAllData().contains(item)) {
                mAdapter.removeData(item);
            }
            mAdapter.addItem(0, item);
            rv_history.scrollToPosition(0);
            SearchHistoryAdapter.SearchItem last = mAdapter.getData(mAdapter.getDataCount() - 1);
            if (last != null && last.getType() == 0) {
                mAdapter.addItem(new SearchHistoryAdapter.SearchItem("清空搜索历史", 1));
            }
        }
    };

    private EditText et_search_point;
    private RecyclerView rv_history;
    private SearchHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerItems = new ArrayList<>();
        mPagerItems.add(new Pair<>("全部笔记", NoteTabFragment.instantiate(this, NoteTabFragment.CATEGORY_ALL)));
        mPagerItems.add(new Pair<>("我的笔记", NoteTabFragment.instantiate(this, NoteTabFragment.CATEGORY_MINE)));
        mPagerItems.add(new Pair<>("分享给我", NoteTabFragment.instantiate(this, NoteTabFragment.CATEGORY_SHARE)));

        viewPager = findViewById(R.id.vp_note);
        mTabs = findViewById(R.id.pager_tabs);
        mTabAdapter = new NoteTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mPagerItems.get(position).second;
            }

            @Override
            public int getCount() {
                return mPagerItems.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mPagerItems.get(position).first;
            }
        });
        viewPager.setOffscreenPageLimit(0);
        mTabs.setShouldExpand(true);
        mTabs.setViewPager(viewPager);
        mTabs.setOnPageChangeListener(this);

        mCurrentFragmentIndex = getIntent().getIntExtra(EXTRA_PAGE, 0);
        switchFragment(mCurrentFragmentIndex);
        RetrofitManager retrofitManager = new RetrofitManager(this);
        tagService = retrofitManager.createService(NoteApp.getBaseUrl(), HttpService.class);

        initWidget();

    }

    private static final String CACHE_NAME = "search_history";

    private void initWidget() {
        rv_history = findViewById(R.id.rv_history);
        mAdapter = new SearchHistoryAdapter(this);
        rv_history.setLayoutManager(new LinearLayoutManager(this));
        rv_history.setAdapter(mAdapter);

        List<SearchHistoryAdapter.SearchItem> items = CacheManagerV2.readListJson(this, CACHE_NAME, SearchHistoryAdapter.SearchItem.class);
        mAdapter.updateData(items);
        if (mAdapter.getItemCount() != 0) {
            mAdapter.addItem(new SearchHistoryAdapter.SearchItem("清空搜索历史", 1));
        }
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchHistoryAdapter.SearchItem item = mAdapter.getData(position);
                if (item != null && item.getType() != 1) {
                    String query=item.getSearchText();
                    et_search_point.setText(query);
                    et_search_point.setSelection(query.length());
                    doSearch(mSearchText);
                }
            }
        });

        et_search_point = findViewById(R.id.et_search_point);
        et_search_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchText = s.toString();
            }
        });
        et_search_point.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(mSearchText);
                }
                return false;
            }
        });
    }

    private void doSearch(String query) {

        mSearchText = query;
        // Always cancel all request
        viewPager.removeCallbacks(mSearchRunnable);
        if (TextUtils.isEmpty(mSearchText)) {
            mTabs.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            rv_history.setVisibility(View.VISIBLE);
            return ;
        }

        mTabs.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
        rv_history.setVisibility(View.GONE);

        // In this we delay 1 seconds
        viewPager.postDelayed(mSearchRunnable, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110 && resultCode == RESULT_OK) {
            mSelectedLabel = (ArrayList<LabellModel>) data.getExtras().getSerializable("selectedLabel");

            if (!mSelectedLabel.isEmpty()) {
                CLog.i(TAG, mSelectedLabel.toString());
            }
        }
    }

    private HttpService tagService;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int index = getIntent().getIntExtra(EXTRA_PAGE, -1);
        if (index != -1) {
            mCurrentFragmentIndex = index;
            switchFragment(index);
        }
    }

    private void switchFragment(int index) {
        mCurrentFragmentIndex = index;
        int item = viewPager.getCurrentItem();
        if (index != mCurrentFragmentIndex) {
            viewPager.setCurrentItem(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class NoteTabAdapter extends FragmentStatePagerAdapter {

        public NoteTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case TAB_ALL:
                    if (allNoteFragment == null) {
                        allNoteFragment = NoteTabFragment
                                .newInstance(NoteTabFragment.CATEGORY_ALL);
                    }
                    return allNoteFragment;

                case TAB_MINE:
                    if (myNoteFragment == null) {
                        myNoteFragment = NoteTabFragment
                                .newInstance(NoteTabFragment.CATEGORY_MINE);
                    }
                    return myNoteFragment;

                case TAB_SHARED:
                    if (shareNoteFragment == null) {
                        shareNoteFragment = NoteTabFragment
                                .newInstance(NoteTabFragment.CATEGORY_SHARE);
                    }
                    return shareNoteFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public interface SearchAction {
        void search(String content);
    }


}
