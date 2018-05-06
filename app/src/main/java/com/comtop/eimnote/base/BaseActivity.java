package com.comtop.eimnote.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.comtop.eimnote.R;
import com.comtop.eimnote.widget.CustomProgressDialog;

/**
 * Author chaos
 * Description:
 * DATE: 2018/5/3
 * Email: oscc92@gmail.com
 */
public class BaseActivity extends FragmentActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgress(R.string.hold_on, true);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissPd();
    }

    protected CustomProgressDialog mProgressDialog;
    public void showProgress() {
        showProgress(R.string.hold_on, true);
    }
    public void showProgress(int resID, boolean canBack) {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.cancel();
            }
            if (BaseActivity.this.isFinishing()) {
                return ;
            }
            mProgressDialog = new CustomProgressDialog(BaseActivity.this,
                    getResources().getString(resID));
            mProgressDialog.setCancelable(canBack);
            mProgressDialog.show();
        } catch (Exception e) {
        }

    }

    public void dismissPd(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        return super.onCreateDialog(id);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        return super.onCreateDialog(id, args);
    }

}
