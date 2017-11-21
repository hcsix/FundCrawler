package com.supcoder.fundcrawler.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.supcoder.fundcrawler.R;
import com.supcoder.fundcrawler.utils.NoDoubleClickUtils;

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
        helper.addOnClickListener(R.id.fundImg);

    }


    private String getUrl(String id) {
        return mContext.getString(R.string.url, id);
    }


}

