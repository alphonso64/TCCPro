package com.example.alphonso.tccpro.app;

import android.app.Application;

import Jack.WewinPrinterHelper.WwPrint;

/**
 * Created by alphonso on 2016/8/14.
 */
public class TApplication extends Application{
    private WwPrint printer;
    public WwPrint getPrinter(){
        if(printer == null)
        {
            printer = new WwPrint();
        }
        return  printer;
    }
}
