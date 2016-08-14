package com.example.alphonso.tccpro.http.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.zxing.WriterException;
import com.google.zxing.client.android.encode.QRCodeEncoder;

/**
 * Created by alphonso on 2016/8/10.
 */
public class BarCodeCreator {
    public static final int FORMAT_4015 = 1;

    public static Bitmap createBarcode(int format, String content)
    {
        Bitmap bitmapBg = null;
        switch (format){
            case FORMAT_4015:
            {
                int width = 38 *8;
                int heightbg = 15*8;
                int heightbarcode = 8*8;
                int hoffset_top = 3*8;

                int hoffset_bottom = 3*8/2;
                int woffset_left = 1*8;
                int woffset_right = 1*8;
                bitmapBg = Bitmap.createBitmap(width, heightbg, Bitmap.Config.ARGB_8888);
                Canvas cv = new Canvas(bitmapBg);
                Paint paint = new Paint();
                paint.setStrokeWidth(1.0f);
                paint.setColor(Color.BLACK);
//                cv.drawLine(woffset_left,hoffset_top,width-woffset_right,hoffset_top,paint);
//                cv.drawLine(woffset_left,hoffset_top,woffset_left,heightbg-hoffset_bottom,paint);
//                cv.drawLine(woffset_left,heightbg-hoffset_bottom,width-woffset_right,heightbg-hoffset_bottom,paint);
//                cv.drawLine(width-woffset_right,hoffset_top,width-woffset_right,heightbg-hoffset_bottom,paint);
                try {
                    Rect recSrc = new Rect(0,0,width-woffset_left-woffset_right,heightbarcode);
                    Rect recDst = new Rect(woffset_left,hoffset_top,width-woffset_right,heightbarcode+hoffset_top);
                    Bitmap image = QRCodeEncoder.encodeCode128AsBitmap(content, width-woffset_left-woffset_right,heightbarcode);//MyBarcode.QS_02F(Texts);
                    cv.drawBitmap(image,recSrc,recDst,paint);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(20);
                    Rect bounds = new Rect();
                    paint.getTextBounds(content, 0, content.length(), bounds);
                    cv.drawText(content,width / 2,13.5f*8,paint);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return bitmapBg;
    }

}
