package com.speedata.kt40scantxt.base;

/**
 * Created by xu on 2016/10/14.
 */

public interface BaseScan {
    /**
     * 获得条码
     *
     * @param barcode
     */
    public void onGetBarcode(String barcode);

    public void onGetByteBarcode(byte[] barcode);
}
