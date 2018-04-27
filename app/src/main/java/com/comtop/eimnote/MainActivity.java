package com.comtop.eimnote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.comtop.eimnote.fragment.NoteTabFragment;
import com.comtop.eimnote.util.DensityUtil;
import com.comtop.eimnote.widget.BadgeView;
import com.comtop.eimnote.widget.NotePagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {

    private NotePagerSlidingTabStrip mTabs;
    private ViewPager viewPager;

    private Fragment allNoteFragment;
    private Fragment myNoteFragment;
    private Fragment shareNoteFragment;
    private NoteTabAdapter mTabAdapter;

    private BadgeView allBadgeView;
    private BadgeView myBadgeView;
    private BadgeView shareBadgeView;

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
    }

    private class NoteTabAdapter extends FragmentStatePagerAdapter {

        public NoteTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (allNoteFragment == null) {
                        allNoteFragment = NoteTabFragment
                                .newInstance(NoteTabFragment.CATEGORY_ALL);
                    }
                    return allNoteFragment;

                case 1:
                    if (myNoteFragment == null) {
                        myNoteFragment = NoteTabFragment
                                .newInstance(NoteTabFragment.CATEGORY_MINE);
                    }
                    return myNoteFragment;

                case 2:
                    if (shareNoteFragment == null) {
                        shareNoteFragment = NoteTabFragment
                                .newInstance(NoteTabFragment.CATEGORY_SHARE);
                    }
                    return shareNoteFragment;
            }
            return null;
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
