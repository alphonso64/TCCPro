package com.example.alphonso.tccpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.alphonso.tccpro.app.TApplication;
import com.example.alphonso.tccpro.http.Util.BarCodeCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alphonso on 2016/8/14.
 */
public class PrintTask extends AsyncTask<Integer, Integer, Integer> {
    Bitmap bitmap;
    ProgressDialog diag;
    Context ctx;

    public PrintTask(Bitmap bitmap, ProgressDialog diag, Context ctx)
    {
        this.bitmap = bitmap;
        this.diag = diag;
        this.ctx = ctx;
    }


    @Override
    protected Integer doInBackground(Integer... params) {

            List<Bitmap> list = new ArrayList<Bitmap>();
            list.add(bitmap);
            int printInt = -1;
            try {
                TApplication app = (TApplication) ctx.getApplicationContext();
                printInt = app.getPrinter().printLable(list, 1, -1, ctx,"");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {

            }
        return printInt;
    }
}
