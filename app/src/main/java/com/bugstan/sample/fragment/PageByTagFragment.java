package com.bugstan.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.bugstan.sample.bean.PagerItemBean;
import com.bugstan.sample.databinding.FragmentPagerByTagBinding;
import com.bugstan.sample.viewmodel.PagerPanelViewModel;

/**
 * paginate by tag, sub page
 */
public class PageByTagFragment extends Fragment {

    private FragmentPagerByTagBinding mViewBinding;

    private static final int PAGE_ITEM_COUNT = 8;

    private final PagerPanelViewModel mViewModel;

    private final String mTagName;
    private final List<List<PagerItemBean>> mPageBean = new ArrayList<>();

    public PageByTagFragment(PagerPanelViewModel viewModel, String tag, List<PagerItemBean> pagerItemBeanList) {
        mViewModel = viewModel;
        mTagName = tag;

        // page item calculate
        Queue<PagerItemBean> tempQueue = new LinkedList<>(pagerItemBeanList);
        while (tempQueue.size() > 0) {
            List<PagerItemBean> itemList = new ArrayList<>();
            for (int j = 0; j < PAGE_ITEM_COUNT; j++) {
                PagerItemBean item = tempQueue.poll();
                if (item == null) break;
                itemList.add(item);
            }
            mPageBean.add(itemList);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FragmentPagerByTagBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewBinding.pagerLayout.setSaveEnabled(false);

        mViewBinding.pagerLayout.setAdapter(new FragmentStateAdapter(PageByTagFragment.this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return new PagerItemFragment(mViewModel, mTagName, mPageBean.get(position));
            }

            @Override
            public int getItemCount() {
                return mPageBean.size();
            }
        });

        // tab dot
        new TabLayoutMediator(mViewBinding.dotLayout, mViewBinding.pagerLayout, (tab, position) -> tab.setText("")).attach();
    }

}