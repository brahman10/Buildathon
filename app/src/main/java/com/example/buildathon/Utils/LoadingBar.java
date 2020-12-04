package com.example.buildathon.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.example.buildathon.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,April,2020
 */
public class LoadingBar {
    Context context;
    Dialog dialog;

    public LoadingBar(Context context) {
        this.context = context;
        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCanceledOnTouchOutside(false);
    }
    public void show()
    {
        if(dialog.isShowing())
        {
            dialog.dismiss();
        }

        dialog.show();
    }

    public void dismiss()
    {
        try
        {
            if(dialog!=null)
            {
                dialog.dismiss();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    public boolean isShowing()
    {
       return dialog.isShowing();
    }
}
