package com.example.alphonso.tccpro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphonso.tccpro.app.TApplication;
import com.example.alphonso.tccpro.http.HttpClient;
import com.example.alphonso.tccpro.http.Util.BarCodeCreator;
import com.example.alphonso.tccpro.http.Util.MyBarcode;
import com.example.alphonso.tccpro.http.Util.StringUtil;
import com.google.zxing.Result;
import com.google.zxing.client.android.decode.CaptureActivity;
import com.google.zxing.client.android.encode.QRCodeEncoder;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.response.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Jack.WewinPrinterHelper.WwPrint;
import biz.kasual.materialnumberpicker.MaterialNumberPicker;

import static com.google.zxing.client.android.encode.QRCodeEncoder.encodeAsBitmap;

public class MainActivity extends CaptureActivity {
    ParsedResult parsedResult;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            List<Bitmap> list = new ArrayList<Bitmap>();
            Bitmap newb =  BarCodeCreator.createBarcode(BarCodeCreator.FORMAT_4015,parsedResult.getDisplayResult());
            list.add(newb);
            int printInt = -1;
            try {
                TApplication app = (TApplication) getApplication();
                printInt = app.getPrinter().printLable(list, msg.what, -1, MainActivity.this,"");
            } catch (Exception e) {
                e.printStackTrace();
            }
//            progressDialog.dismiss();
            switch (printInt)
            {
                case -1:
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "未知错误", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                case 0:
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "蓝牙未开启", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                case 1:
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "打印机未匹配", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                case 2:
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "打印机连接失败", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                case 3:
                {
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "打印成功", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                    break;
                }
                default:
            }
        }};

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        super.handleDecode(rawResult, barcode, scaleFactor);

        parsedResult=  ResultParser.parseResult(rawResult);
        flag = barcode != null;
        if (flag) {
            // Then not from history, so beep/vibrate and we have an image to draw on
            beepManager.playBeepSoundAndVibrate();
//            drawResultPoints(barcode, scaleFactor, rawResult);
            resultView.setVisibility(View.VISIBLE);
            viewfinderView.setVisibility(View.GONE);
            ImageView barcodeImageView = (ImageView) findViewById(com.google.zxing.client.android.R.id.barcode_image_view);
            barcodeImageView.setImageBitmap(barcode);

            TextView formatTextView = (TextView) findViewById(com.google.zxing.client.android.R.id.format_text_view);
            formatTextView.setText(rawResult.getBarcodeFormat().toString());

            TextView typeTextView = (TextView) findViewById(com.google.zxing.client.android.R.id.type_text_view);
            typeTextView.setText(parsedResult.getType().toString());

            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            TextView timeTextView = (TextView) findViewById(com.google.zxing.client.android.R.id.time_text_view);
            timeTextView.setText(formatter.format(new Date(rawResult.getTimestamp())));

            TextView contentView = (TextView) findViewById(com.google.zxing.client.android.R.id.content_text_view);
            contentView.setText(parsedResult.getDisplayResult());

//            parseByHttp(parsedResult.getDisplayResult());
        }
        beepManager.playBeepSoundAndVibrate();

    }

    private void parseByHttp(String content){
        HttpListener listener =  new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                View v1 =   findViewById(R.id.parse_indicate_view);
                v1.setVisibility(View.GONE);
                View v2 =   findViewById(R.id.parse_content_view);
                v2.setVisibility(View.VISIBLE);
                try {
                    TextView tx = (TextView)findViewById(R.id.parse_text_view) ;
                    JSONObject object = new JSONObject(s);
                    String result = (String) object.get("return_code");
                    if(result.equals("success")){
                        tx.setText(object.getString("return_msg"));
                    }else{
                        tx.setText("无数据记录");
                    }
                } catch (JSONException e) {
                    return;
                }
            }
            @Override
            public void onFailure(HttpException e, Response<String> response) {

            }
        };
        HttpClient.getInstance().parse(listener,content);
    }



    @Override
    public void detectClick(View V) {
        super.detectClick(V);
        flag = false;
        resultView.setVisibility(View.GONE);
        viewfinderView.setVisibility(View.VISIBLE);
        restartPreviewAfterDelay(0);
    }

    @Override
    public void printClick(View V) {
        super.detectClick(V);

        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(MainActivity.this)
                .minValue(1)
                .maxValue(10)
                .defaultValue(1)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();
        new AlertDialog.Builder(this)
                .setTitle("选择打印份数")
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        progressDialog = ProgressDialog.show(MainActivity.this, null, "正在连接打印机",
//                                false, false);
                        handler.sendEmptyMessage(numberPicker.getValue());
                    }
                }).show();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(flag)
            {
                flag = false;
                resultView.setVisibility(View.GONE);
                viewfinderView.setVisibility(View.VISIBLE);
                restartPreviewAfterDelay(0);
                return  true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }
}
