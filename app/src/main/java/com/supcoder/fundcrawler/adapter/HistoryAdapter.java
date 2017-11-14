package com.supcoder.fundcrawler.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.supcoder.fundcrawler.R;
import com.supcoder.fundcrawler.entity.HistoryEntity;

import java.util.List;

/**
 * HistoryAdapter
 *
 * @author lee
 * @date 2017/11/14
 */

public class HistoryAdapter extends BaseQuickAdapter<HistoryEntity, BaseViewHolder> {

    private OnHistoryListener onHistoryListener;


    public HistoryAdapter(@Nullable List<HistoryEntity> data,OnHistoryListener onHistoryListener) {
        super(R.layout.item_history, data);
        this.onHistoryListener = onHistoryListener;

    }


    public void refresh(List<HistoryEntity> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryEntity item) {

        Log.e("HistoryAdapter", item.getFundId());
        helper.setText(R.id.historyTv, item.getFundId());

        helper.getView(R.id.cancelImg).setOnClickListener(view -> {
            if (onHistoryListener != null) {
                onHistoryListener.onDelete(item);
            }
        });

        helper.getConvertView().setOnClickListener(view -> {
            if (onHistoryListener != null) {
                onHistoryListener.onFund(item.getFundId());
            }
        });
    }


    public interface OnHistoryListener {
        /**
         * 删除
         *
         * @param fund
         */
        void onDelete(HistoryEntity fund);


        /**
         * 点击了选项
         * @param fundId
         */
        void onFund(String fundId);
    }
}
