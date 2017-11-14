package com.supcoder.fundcrawler.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.supcoder.fundcrawler.R;

import java.util.List;

/**
 * ImageAdapter
 *
 * @author lee
 * @date 2017/11/13
 */

public class ImageAdapter extends BaseItemDraggableAdapter<String, BaseViewHolder> {
    public ImageAdapter(List data) {
        super(R.layout.item_img, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext)
                .load(getUrl(item))
                .into((ImageView) helper.getView(R.id.fundImg));
    }


    private String getUrl(String id) {
        return mContext.getString(R.string.url, id);
    }
}

