package com.example.facmanager;

import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RequestHttpUrlConnection {

    private static RequestHttpUrlConnection mInstance = null;
    //private static CookieManager cookieManager = new CookieManager();

    private String CharSet = "UTF-8";

    private RequestHttpUrlConnection() {

    }

    public static RequestHttpUrlConnection getInstance() {

        if(mInstance == null) mInstance = new RequestHttpUrlConnection();

        //CookieHandler.setDefault(cookieManager);
        //cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        return mInstance;

    }

    private String getParameterStr(ContentValues _params) {
        StringBuffer stringBuffer = new StringBuffer();

        if(_params == null) stringBuffer.append("");
        else {

            for(Map.Entry<String, Object> params : _params.valueSet()) {

                String key = params.getKey();
                String value = params.getValue().toString();

                stringBuffer.append(key).append("=").append(value).append("&");


            }

        }

        return stringBuffer.toString();
    }

    private String getCookieStr() {

        /*List<HttpCookie> cookieList = CookieHandler.getDefault().getCookieStore().getCookies();

        if(cookieList.size() > 0) {

            StringBuffer cookieStringBuffer = new StringBuffer();

            for(HttpCookie cookie : this.cookieManager.getCookieStore().getCookies()) {

                cookieStringBuffer.append(cookie.getName()).append("=").append(cookie.getValue()).append(';');

            }

            return cookieStringBuffer.toString();

        }*/

        List<HttpCookie> cookies = ((CookieManager) CookieHandler.getDefault()).getCookieStore().getCookies();
        for (HttpCookie cookie : cookies) {
            System.out.println(cookie.getDomain());
            System.out.println(cookie);
        }

        return "";
    }

    public String requestGET(String _url, ContentValues _params) {

        // 요청부

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {

            _url = _url + "?" + getParameterStr(_params);

            URL url = new URL(_url);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connection 속성
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept-Charset", this.CharSet);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="+this.CharSet);

            // 쿠키 설정
            urlConnection.setRequestProperty("Cookie", getCookieStr());

            // 응답 받아오기
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, this.CharSet));

            String line;
            StringBuilder body = new StringBuilder();

            while((line = bufferedReader.readLine()) != null) {

                body.append(line);

            }

            return body.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;

    }

    public String requestPOST(String _url, ContentValues _params) {

        // 요청부

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(_url);

            urlConnection = (HttpURLConnection) url.openConnection();

            //CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            // Connection 속성
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept-Charset", this.CharSet);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="+this.CharSet);

            Log.d("cookie", "c- : "+getCookieStr());

            // 파라미터 쓰기
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(getParameterStr(_params).getBytes(this.CharSet));
            outputStream.flush();
            outputStream.close();

            // 응답 받아오기
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }

            System.out.println(urlConnection.getHeaderFields());

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, this.CharSet));

            String line;
            StringBuilder body = new StringBuilder();

            while((line = bufferedReader.readLine()) != null) {

                body.append(line);

            }

            return body.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;

    }

}
