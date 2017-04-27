package com.speedata.kt40scantxt.adapter;

import android.content.Context;

import com.speedata.kt40scantxt.R;
import com.speedata.kt40scantxt.bean.OutputOne;

import java.util.List;

import xyz.reginer.baseadapter.BaseAdapterHelper;
import xyz.reginer.baseadapter.CommonRvAdapter;

/**
 * Created by xu on 2017/3/9.
 */

public class OneAdapter extends CommonRvAdapter<OutputOne> {
    private List<OutputOne> mList;

    public OneAdapter(Context context, int layoutResId, List<OutputOne> data) {
        super(context, layoutResId, data);
        mList = data;
    }


    @Override
    public void convert(BaseAdapterHelper helper, OutputOne item, int position) {
        helper.setText(R.id.tv_one_line1, item.getNumber());

    }
}
