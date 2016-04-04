package com.flylighten.app.gameheadline.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.ui.fragment.NewsListFragment;

import in.srain.cube.app.CubeFragment;
import in.srain.cube.views.pager.TabPageIndicator;

public class PagerTabIndicatorFragment extends CubeFragment {
    private static final String[] CONTENT = new String[]{"最新", "热门", "兴趣"};

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_news_tabs, null);

        TabPageAdapter adapter = new TabPageAdapter(getFragmentManager());

        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewHolderCreator(new TabPageIndicator.ViewHolderCreator() {
            @Override
            public TabPageIndicator.ViewHolderBase createViewHolder() {
                return new DemoViewHolder();
            }
        });
        indicator.setViewPager(pager, 0);

        return view;
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
