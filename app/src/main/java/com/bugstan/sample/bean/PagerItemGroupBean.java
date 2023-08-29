package com.bugstan.sample.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Item group bean with tag/label properties
 */
public class PagerItemGroupBean implements Serializable {

    private List<PagerItemBean> itemList;
    private int tagId;
    private String tagName;

    public List<PagerItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<PagerItemBean> itemList) {
        this.itemList = itemList;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

}
