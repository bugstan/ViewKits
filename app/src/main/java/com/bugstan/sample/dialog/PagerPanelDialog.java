package com.bugstan.sample.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bugstan.sample.bean.PagerItemGroupBean;
import com.bugstan.sample.databinding.DialogPagerPanelBinding;
import com.bugstan.sample.fragment.PageByTagFragment;
import com.bugstan.sample.viewmodel.PagerPanelViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

/**
 * ViewPager2 panel example:
 * demonstrates ViewPager2 nested inside ViewPager2
 */
@SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
public class PagerPanelDialog extends DialogFragment {

    private DialogPagerPanelBinding mViewBinding;

    private PagerPanelViewModel mViewModel;

    /**
     * Constructor
     */
    public PagerPanelDialog() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        mViewModel = new ViewModelProvider(this).get(PagerPanelViewModel.class);

        mViewModel.getPanelDataLiveData().observe(this, new Observer<List<PagerItemGroupBean>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<PagerItemGroupBean> dataList) {
                updateUI(dataList);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = DialogPagerPanelBinding.inflate(inflater, container, false);
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // mViewBinding.viewPager.setSaveEnabled(false);
        mViewBinding.viewPager.setOffscreenPageLimit(2);
        mViewModel.requestPagerPanelDataList();
    }

    /**
     * draw the ViewPager and tabs
     */
    private void updateUI(List<PagerItemGroupBean> dataList) {
        if (dataList == null) return;
        if (dataList.size() == 0) return;

        FragmentStateAdapter fragmentStateAdapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                PagerItemGroupBean pagerItemGroupBean = dataList.get(position);
                return new PageByTagFragment(mViewModel, pagerItemGroupBean.getTagName(), pagerItemGroupBean.getItemList());
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }

            @Override
            public boolean containsItem(long itemId) {
                return super.containsItem(itemId);
            }
        };

        mViewBinding.viewPager.setAdapter(fragmentStateAdapter);

        new TabLayoutMediator(mViewBinding.tabLayout, mViewBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                PagerItemGroupBean pagerItemGroupBean = dataList.get(position);
                tab.setText(pagerItemGroupBean.getTagName());
            }
        }).attach();
    }

}
