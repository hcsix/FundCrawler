package com.supcoder.fundcrawler.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.supcoder.fundcrawler.R;
import com.supcoder.fundcrawler.http.ProcessMessege;

import java.util.List;

/**
 * BottomSheetAdapter
 *
 * @author lee
 * @date 2017/11/17
 */

public class BottomSheetAdapter extends BaseQuickAdapter<ProcessMessege, BaseViewHolder> {
    public BottomSheetAdapter(@Nullable List<ProcessMessege> data) {
        super(R.layout.item_position_status, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProcessMessege item) {

        helper.setText(R.id.nameTv, item.getProcessName())
                .setText(R.id.cczbTv, item.getProcessScale())
                .setText(R.id.zdfTv, item.getFluctuate())
                .setTextColor(R.id.zdfTv, ContextCompat.getColor(mContext,
                        item.getFluctuate().contains("-") ?
                                R.color.green : R.color.red)
                )
        ;
    }
}
