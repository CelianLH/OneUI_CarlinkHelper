package com.baidu.BaiduMap.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Utils.DonateUtils;
import com.baidu.BaiduMap.Utils.PreferenceUtil;

public class FirstBootDialog extends DialogFragment {
    private Context mContext;


    private DialogListener dialogListener = new DialogListener() {
        @Override
        public void onResult() {

        }
    };

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setMessage(R.string.message_first_boot)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CarLinkData.putBoolean(mContext,"confirmed",true);
                    }
                });
        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
    }



    public interface DialogListener {
        void onResult();
    }
}
