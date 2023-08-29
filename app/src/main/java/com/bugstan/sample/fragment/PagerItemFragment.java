package com.bugstan.sample.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import com.bugstan.sample.adapter.PagerDataAdapter;
import com.bugstan.sample.bean.PagerItemBean;
import com.bugstan.sample.databinding.FragmentPagerItemBinding;
import com.bugstan.sample.viewmodel.PagerPanelViewModel;

/**
 * show the item on this page
 */
@SuppressWarnings("Convert2Lambda")
public class PagerItemFragment extends Fragment {

    private FragmentPagerItemBinding mViewBinding;

    // GridLayout, item size per line
    private static final int NUM_ITEMS_PER_LINE = 4;

    private final PagerPanelViewModel mViewModel;

    private final List<PagerItemBean> mPagerItemList;
    // Adapter
    private PagerDataAdapter mPagerDataAdapter;

    public PagerItemFragment(@NonNull PagerPanelViewModel viewModel, String tag, @NonNull List<PagerItemBean> pagerItemBeanList) {
        mViewModel = viewModel;
        mPagerItemList = pagerItemBeanList;

        // for item count > 4
        while (mPagerItemList.size() < 5) {
            mPagerItemList.add(new PagerItemBean());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel.getSelectItemLiveData().observe(this, new Observer<PagerItemBean>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(PagerItemBean pagerItemBean) {
                for (PagerItemBean bean : mPagerItemList) {
                    if (bean != null)
                        bean.setSelected(pagerItemBean.getId().equals(bean.getId()));
                }
                mPagerDataAdapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinding = FragmentPagerItemBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mPagerDataAdapter = new PagerDataAdapter();

        mPagerDataAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                PagerItemBean pagerItemBean = (PagerItemBean) adapter.getItem(position);
                mViewModel.notifyItemChanged(pagerItemBean.getId());
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), NUM_ITEMS_PER_LINE);
        mViewBinding.itemRecyclerView.setLayoutManager(gridLayoutManager);

        // disable animation
        if (mViewBinding.itemRecyclerView.getItemAnimator() != null)
            mViewBinding.itemRecyclerView.getItemAnimator().setChangeDuration(0);

        mViewBinding.itemRecyclerView.setAdapter(mPagerDataAdapter);

        mPagerDataAdapter.setList(mPagerItemList);
    }
}