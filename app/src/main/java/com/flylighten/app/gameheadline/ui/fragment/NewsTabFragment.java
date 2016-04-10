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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flylighten.app.gameheadline.R;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.app.CubeFragment;
import in.srain.cube.mints.base.TitleHeaderBar;
import in.srain.cube.views.pager.TabPageIndicator;

public class NewsTabFragment extends CubeFragment {
    private static final String[] CONTENT = new String[]{"最新", "本周热榜", "本月热榜"};
    private final List<Fragment> mFragmentList = new ArrayList<>();


    void initNewsList(View view) {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_news_tabs, null);
        initNewsList(view);
        return view;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    class TabPageAdapter extends FragmentPagerAdapter {


        public TabPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (0 == position) {
                return new LatestNewsListFragment();
            } else if (1 == position) {
                HotNewsListFragment item = new HotNewsListFragment();
                item.set_type(HotNewsListFragment.HOT_TYPE.HOT_TYPE_MONTHLY);
                return item;
            } else if (2 == position) {
                HotNewsListFragment item = new HotNewsListFragment();
                item.set_type(HotNewsListFragment.HOT_TYPE.HOT_TYPE_MONTHLY);
                return item;
            } else {
                return new LatestNewsListFragment();
            }
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
