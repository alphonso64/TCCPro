package com.example.alphonso.tccpro.http;

import android.util.Log;

import com.litesuits.http.LiteHttp;
import com.litesuits.http.data.GsonImpl;
import com.litesuits.http.impl.huc.HttpUrlClient;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.StringBody;
import com.litesuits.http.request.param.HttpMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by alphonso on 2016/7/28.
 */
public class HttpClient {
    private LiteHttp liteHttp;
    private static HttpClient single = null;

    private static final String DOMAIN_NAME = "http://115.149.152.49/";

    //登陆判断
    public static final String LOGIN_URL = DOMAIN_NAME + "supervise/loginFromPhone.do";

    private HttpClient() {
        liteHttp = LiteHttp.build(null)
                .setHttpClient(new HttpUrlClient())       // http
                .setUserAgent("Mozilla/5.0 (...)")  // set custom User-Agent
                .setSocketTimeout(5000)           // socket timeout: 10s
                .setConnectTimeout(5000)         // connect timeout: 10s
                .create();
    }

    public static HttpClient getInstance() {
        if (single == null) {
            single = new HttpClient();
        }
        return single;
    }

    public void checkLogin(HttpListener<String> listener, String name, String pwd) {
        if (listener == null || name == null || pwd == null) return;

        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("contentType", "utf-8");
        header.put("Content-type", "application/x-java-serialized-object");
        StringRequest ss = new StringRequest("http://115.29.193.236/rest/web/index.php?name=测试").setHeaders(header)
                .setMethod(HttpMethods.Get).setHttpBody(new StringBody("")).setHttpListener(listener);
        liteHttp.executeAsync(ss);
        Log.e("tcc",ss.getHttpBody().toString());
    }

    public void parse(HttpListener<String> listener, String content) {
        if (listener == null || content == null) return;

        String path = "http://115.29.193.236/rest/web/index.php?name="+content;
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("contentType", "utf-8");
        header.put("Content-type", "application/x-java-serialized-object");
        StringRequest ss = new StringRequest(path).setHeaders(header)
                .setMethod(HttpMethods.Get).setHttpBody(new StringBody("")).setHttpListener(listener);
        liteHttp.executeAsync(ss);
        Log.e("tcc",ss.getHttpBody().toString());
    }

}
