package com.comtop.eimnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.comtop.eimnote.fragment.NoteTabFragment;
import com.comtop.eimnote.util.DensityUtil;
import com.comtop.eimnote.widget.BadgeView;
import com.comtop.eimnote.widget.NotePagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

    public static final String EXTRA_PAGE = "page";
    public static final String EXTRA_SOURCE = "source";
    public static final int TAB_ALL = 0;
    public static final int TAB_MINE = 1;
    public static final int TAB_SHARED = 2;

    private NotePagerSlidingTabStrip mTabs;
    private ViewPager viewPager;

    private Fragment allNoteFragment;
    private Fragment myNoteFragment;
    private Fragment shareNoteFragment;
    private NoteTabAdapter mTabAdapter;

    private BadgeView allBadgeView;
    private BadgeView myBadgeView;
    private BadgeView shareBadgeView;
    private int mCurrentFragmentIndex = 0;

    private final String[] tabs = new String[]{"全部笔记", "我的笔记", "分享笔记"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.vp_note);
        mTabs = findViewById(R.id.pager_tabs);
        mTabAdapter = new NoteTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mTabAdapter);
        viewPager.setOffscreenPageLimit(3);
        mTabs.setShouldExpand(true);
        mTabs.setViewPager(viewPager);
        mTabs.setOnPageChangeListener(pageChangeListener);

        createAllBadgeView();
        createmyBadgeView();
        createShareBadgeView();

        mCurrentFragmentIndex = getIntent().getIntExtra(EXTRA_PAGE, 0);
        switchFragment(mCurrentFragmentIndex);

    }

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

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager
            .OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 2:
                    shareBadgeView.hide();
                    break;
                case 0:
                    allBadgeView.hide();
                    break;
                case 1:
                    myBadgeView.hide();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void createAllBadgeView() {
        if (allBadgeView == null) {
            allBadgeView = createBadgeView(tabs.length - 3);
        }
        allBadgeView.show();
    }

    private void createShareBadgeView() {
        if (shareBadgeView == null) {
            shareBadgeView = createBadgeView(tabs.length - 1);
        }
    }

    private void createmyBadgeView() {
        if (myBadgeView == null) {
            myBadgeView = createBadgeView(tabs.length - 2);
        }
    }

    private BadgeView createBadgeView(int index) {
        TextView targetView = (TextView) mTabs.getTabsContainer().getChildAt(index);
        if (targetView == null)
            return null;
        BadgeView badgeView = new BadgeView(this, targetView);
        badgeView.setWidth(DensityUtil.dip2px(6));
        badgeView.setHeight(DensityUtil.dip2px(6));
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeView.setBadgeMargin(DensityUtil.dip2px(30),
                DensityUtil.dip2px(8));
        return badgeView;
    }

    private boolean isAllRead() {
        return !allBadgeView.isShown()
                && !shareBadgeView.isShown()
                && !myBadgeView.isShown();
    }


}
