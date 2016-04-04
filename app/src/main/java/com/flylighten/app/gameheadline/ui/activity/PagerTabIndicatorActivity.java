package com.flylighten.app.gameheadline.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.ui.fragment.NewsListFragment;

import in.srain.cube.app.CubeFragmentActivity;
import in.srain.cube.views.pager.TabPageIndicator;

public class PagerTabIndicatorActivity extends CubeFragmentActivity {
    private static final String[] CONTENT = new String[]{"最新", "热门", "兴趣"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        TabPageAdapter adapter = new TabPageAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewHolderCreator(new TabPageIndicator.ViewHolderCreator() {
            @Override
            public TabPageIndicator.ViewHolderBase createViewHolder() {
                return new DemoViewHolder();
            }
        });
        indicator.setViewPager(pager, 0);
    }

    @Override
    protected String getCloseWarning() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.layout.activity_tabs;
    }

    class TabPageAdapter extends FragmentPagerAdapter {

        public TabPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new NewsListFragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }

    private class DemoViewHolder extends TabPageIndicator.ViewHolderBase {

        private TextView mTitleTextView;
        private View mViewSelected;
        private final int COLOR_TEXT_SELECTED = Color.parseColor("#ffffff");
        private final int COLOR_TEXT_NORMAL = Color.parseColor("#BDBDBD");

        @Override
        public View createView(LayoutInflater layoutInflater, int position) {
            View view = layoutInflater.inflate(R.layout.ht_views_tab_indicator, null);
            mTitleTextView = (TextView) view.findViewById(R.id.ht_views_tab_item_title);
            mViewSelected = view.findViewById(R.id.ht_views_tab_item_selected);
            return view;
        }

        @Override
        public void updateView(int position, boolean isCurrent) {
            mTitleTextView.setText(CONTENT[position]);
            if (isCurrent) {
                mTitleTextView.setTextColor(COLOR_TEXT_SELECTED);
                mViewSelected.setVisibility(View.VISIBLE);
            } else {
                mTitleTextView.setTextColor(COLOR_TEXT_NORMAL);
                mViewSelected.setVisibility(View.INVISIBLE);
            }
        }
    }
}
