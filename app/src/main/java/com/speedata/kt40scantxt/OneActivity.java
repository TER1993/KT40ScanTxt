package com.speedata.kt40scantxt;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.speedata.kt40scantxt.adapter.OneAdapter;
import com.speedata.kt40scantxt.base.BaseScan;
import com.speedata.kt40scantxt.bean.OutputOne;
import com.speedata.kt40scantxt.utils.FileUtils;
import com.speedata.kt40scantxt.utils.MyDateAndTime;
import com.speedata.kt40scantxt.utils.PlaySoundPool;
import com.speedata.kt40scantxt.utils.ScanUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xuyan
 */
public class OneActivity extends Activity implements BaseScan, View.OnClickListener {
    protected TextView mBarTitle;
    protected ImageView mBarLeft;
    /**
     * 单件扫描不计数，不允许重复
     */
    private List<OutputOne> mList;
    private OneAdapter mAdapter;
    private Button btnOne;
    /**
     * 扫描录入SN
     */
    private ScanUtil scanUtil;
    /**
     * 按退出时弹出对话框
     */
    private AlertDialog mExitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_one);
        initTitle();
        initView();
    }

    private void initView() {
        mList = new ArrayList<>();
        btnOne = (Button) findViewById(R.id.btn_one_save);
        btnOne.setOnClickListener(this);
        mBarLeft.setOnClickListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_one_content);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter横线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new OneAdapter(this, R.layout.view_one_item_layout, mList);

        mRecyclerView.setAdapter(mAdapter);


        // 创建退出时的对话框，此处根据需要显示的先后顺序决定按钮应该使用Neutral、Negative或Positive
        DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
        mExitDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.out_title)
                .setMessage(R.string.out_message)
                .setCancelable(false)
                .setNeutralButton(R.string.out_sure, dialogButtonOnClickListener)
                .setPositiveButton(R.string.out_miss, dialogButtonOnClickListener)
                .create();

    }

    private void initTitle() {
        setNavigation(1, getString(R.string.one_title));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断是否按下“BACK”(返回)键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 弹出退出时的对话框
            mExitDialog.show();
            // 返回true以表示消费事件，避免按默认的方式处理“BACK”键的事件
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 退出时的对话框的按钮点击事件
     */
    private class DialogButtonOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // 取消
                case DialogInterface.BUTTON_POSITIVE:
                    // 取消显示对话框
                    mExitDialog.dismiss();
                    break;
                // 退出程序
                case DialogInterface.BUTTON_NEUTRAL:
                    // 结束，将导致onDestroy()方法被回调
                    finish();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 扫描获取SN将扫描结果显示在界面上
     */
    @Override
    protected void onResume() {
        scanUtil = new ScanUtil(this);
        scanUtil.setOnScanListener(new ScanUtil.OnScanListener() {
            @Override
            public void getBarcode(String barcode) {

                onGetBarcode(barcode);

            }

            @Override
            public void getByteBarcode(byte[] barcode) {

                onGetByteBarcode(barcode);

            }
        });
        super.onResume();
    }

    /**
     * 停止扫描
     */
    @Override
    protected void onPause() {
        scanUtil.stopScan();
        super.onPause();
    }

    /**
     * 扫描结果去除回车并处理
     */
    @Override
    public void onGetBarcode(String barcode) {

        barcode = barcode.replace("\n", "");
        barcode = barcode.replace("\u0000", "").replace("\r", "");

        //做数据处理  重复的提示音，不重复的显示出来
        if (mList.size() == 0) {
            OutputOne bean = new OutputOne();
            bean.setNumber(barcode);
            mList.add(bean);
            PlaySoundPool.getPlaySoundPool(this).playLaser();
        } else {
            for (int i = 0; i < mList.size(); i++) {
                if (barcode.equals(mList.get(i).getNumber())) {
                    //有相同的，提示音并且不添加并且返回
                    Toast.makeText(this, R.string.code_hade, Toast.LENGTH_SHORT).show();
                    PlaySoundPool.getPlaySoundPool(this).playError();
                    return;
                }

            }
            OutputOne bean = new OutputOne();
            bean.setNumber(barcode);
            mList.add(bean);
            PlaySoundPool.getPlaySoundPool(this).playLaser();

        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetByteBarcode(byte[] barcode) {
        Log.d("test", Arrays.toString(barcode));
    }


    /**
     * 这是导航
     *
     * @param left  左侧图标
     * @param title 标题
     */
    protected void setNavigation(int left, String title) {
        mBarTitle = (TextView) findViewById(R.id.tv_bar_title);
        mBarLeft = (ImageView) findViewById(R.id.iv_left);
        if (!TextUtils.isEmpty(title)) {
            mBarTitle.setText(title);
        }
        mBarLeft.setVisibility(left == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_one_save:
                //导出为txt文件，制作文件名
                outPutFile();
                break;
            case R.id.iv_left:
                finish();
                break;
            default:
                break;
        }
    }

    private void outPutFile() {
        FileUtils fileUtils = new FileUtils();
        int h = fileUtils.outputOnefile(mList, createFilename());
        if (h == 1) {
            Toast.makeText(this, R.string.success_output, Toast.LENGTH_SHORT).show();
        }
        MainActivity.scanFile(OneActivity.this, createFilename());
    }

    /**
     * 创建导出文件的名字
     *
     * @return 完整文件路径+名
     */
    public String createFilename() {
        String checktime = MyDateAndTime.getMakerDate();
        //获得年月日
        String date = checktime.substring(0, 8);
        //获得年月日
        String time = checktime.substring(8, 12);
        String name = getString(R.string.singleton_) + date + "_" + time;
        return getString(R.string.export_path_) + name + getString(R.string.txt);
    }


}
