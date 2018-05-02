package com.comtop.eimnote.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.comtop.eimnote.R;


public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context, String strMessage) {
        this(context, R.style.CustomProgressDialog, strMessage);
    }

    TextView tvMsg = null;

    public CustomProgressDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        this.setContentView(R.layout.view_progress_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
        setMessage(strMessage);
    }

    public void setMessage(String strMessage) {
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        /*if (getOwnerActivity() == null) return;*/
        if (!hasFocus) {
            dismiss();
        }
    }
    
}
