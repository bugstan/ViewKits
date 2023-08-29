package com.bugstan.sample.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import com.bugstan.sample.R;
import com.bugstan.sample.bean.PagerItemBean;

/**
 * The item adapter for recycleView
 */
public class PagerDataAdapter extends BaseQuickAdapter<PagerItemBean, BaseViewHolder> {

    public PagerDataAdapter() {
        super(R.layout.item_sample_gift);
        setHasStableIds(true);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, PagerItemBean data) {
        // root view
        LinearLayout itemView = holder.getView(R.id.item_root);
        if (data == null) {
            itemView.setVisibility(View.INVISIBLE);
            return;
        }
        if (data.getId() == null) {
            itemView.setVisibility(View.INVISIBLE);
            return;
        }

        itemView.setVisibility(View.VISIBLE);

        itemView.setBackgroundResource(data.isSelected() ? R.drawable.bg_border_16_green : R.drawable.bg_transparent);

        ImageView itemImageView = holder.getView(R.id.item_image);
        if (data.getId().startsWith("2")) {
            itemImageView.setImageResource(R.mipmap.ic_gift_translucent);
        } else {
            itemImageView.setImageResource(data.getId().startsWith("1") ? R.mipmap.ic_gift_box : R.mipmap.ic_gold_coin);
        }

        TextView nameView = holder.getView(R.id.item_name);

        nameView.setFocusableInTouchMode(true);
        nameView.setFocusable(true);
        nameView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        nameView.setText(data.getName());
        nameView.setSelected(data.isSelected());

        if (data.isSelected()) {
            nameView.requestFocus();
        }

    }
}
