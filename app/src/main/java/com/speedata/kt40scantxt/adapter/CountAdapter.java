package com.speedata.kt40scantxt.adapter;

import android.content.Context;

import com.speedata.kt40scantxt.R;
import com.speedata.kt40scantxt.bean.OutputCount;

import java.util.List;

import xyz.reginer.baseadapter.BaseAdapterHelper;
import xyz.reginer.baseadapter.CommonRvAdapter;

/**
 * Created by xu on 2017/3/9.
 */

public class CountAdapter extends CommonRvAdapter<OutputCount> {
    private List<OutputCount> mList;

    public CountAdapter(Context context, int layoutResId, List<OutputCount> data) {
        super(context, layoutResId, data);
        mList = data;
    }



    @Override
    public void convert(BaseAdapterHelper helper, OutputCount item, int position) {
        helper.setText(R.id.tv_count_line1, item.getNumber());
        helper.setText(R.id.tv_count_line2, item.getCount());
        setOnItemChildClickListener(helper, position, R.id.tv_count_line2);
    }
}
