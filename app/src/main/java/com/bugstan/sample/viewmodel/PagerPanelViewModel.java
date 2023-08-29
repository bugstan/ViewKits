package com.bugstan.sample.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bugstan.sample.bean.PagerItemBean;
import com.bugstan.sample.bean.PagerItemGroupBean;

/**
 * Panel ViewModel
 */
public class PagerPanelViewModel extends ViewModel {

    // Selected Item LiveData
    private MutableLiveData<PagerItemBean> mSelectItemLiveData = new MutableLiveData<>();

    // Panel data LiveData
    private MutableLiveData<List<PagerItemGroupBean>> mPaneDataLiveData = new MutableLiveData<>();

    // Pager panel data
    private final List<PagerItemGroupBean> mPagerPanelData;

    public PagerPanelViewModel() {
        mPagerPanelData = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            PagerItemGroupBean groupBean = new PagerItemGroupBean();
            groupBean.setTagId(i);
            groupBean.setTagName("Group" + i);

            List<PagerItemBean> itemList = new ArrayList<>();
            for (int j = 1; j < 30; j++) {
                PagerItemBean pagerItemBean = new PagerItemBean();
                String fakeId = String.format(Locale.getDefault(), "%d%02d", i, j);
                pagerItemBean.setId(fakeId);
                pagerItemBean.setName(String.format(Locale.getDefault(), "%d%02d", i, j));
                itemList.add(pagerItemBean);
            }
            groupBean.setItemList(itemList);

            mPagerPanelData.add(groupBean);
        }
    }

    /**
     * selected status LiveData
     */
    public MutableLiveData<PagerItemBean> getSelectItemLiveData() {
        if (mSelectItemLiveData == null)
            mSelectItemLiveData = new MutableLiveData<>();
        return mSelectItemLiveData;
    }

    /**
     * get panel data LiveData
     */
    public MutableLiveData<List<PagerItemGroupBean>> getPanelDataLiveData() {
        if (mPaneDataLiveData == null)
            mPaneDataLiveData = new MutableLiveData<>();
        return mPaneDataLiveData;
    }

    /**
     * request pager panel fake data
     */
    public void requestPagerPanelDataList() {
        getPanelDataLiveData().postValue(mPagerPanelData);
    }

    /**
     * notify the item select status is changed
     *
     * @param itemId String
     */
    public void notifyItemChanged(String itemId) {
        PagerItemBean selectedItem = null;
        for (PagerItemGroupBean groupBean : mPagerPanelData) {
            for (PagerItemBean bean : groupBean.getItemList()) {
                if (bean.getId().equals(itemId)) {
                    selectedItem = bean;
                }
                bean.setSelected(bean.getId().equals(itemId));
            }
        }
        if (selectedItem != null) {
            getSelectItemLiveData().postValue(selectedItem);
        }
    }
}
